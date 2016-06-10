package com.evmobile.eversvik.stationmonitor.src.activity;

import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.evmobile.eversvik.stationmonitor.src.util.XmlUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

/**
 * Created by eversvik on 08.02.2016.
 * Create a connection to a ZAP server
 * Handles subscription and receiving/sending ZAP data
 */
public class ZapTcpCon extends Thread {
    private static final String TAG = "zaptcpcon";
    private String incomingMessage, bufferedMessage;
    BufferedReader in;
    PrintWriter out;

    private boolean mRun = false;
    Messenger messenger;
    String id;
    String ipNumber;
    DocumentBuilder dBuilder;
    HashMap<String, Document> subs = new HashMap<String, Document>();

    private Document getOrCreateDocument(String spath)
    {
        Document d = subs.get(spath);
        if(d == null)
        {
            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            try {

                dBuilder = dbFactory.newDocumentBuilder();
                d = dBuilder.newDocument();
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                return null;
            }
            subs.put(spath, d);
        }

        return d;
    }

    public ZapTcpCon(Messenger messenger, String ipNumber, String id)  {

        this.ipNumber = ipNumber;
        this.messenger = messenger;
        this.id = id;
    }

    public void sendMessage(String message) {
        if (out != null && !out.checkError()) {
            out.println(message);
            out.flush();
            Log.d(TAG, "Sent Message: " + message);
        }
    }

    public void stopClient() {
        Log.d(TAG, "Request that client should stop");
        mRun = false;
    }

    public Document getDocument(String path)
    {
        return getOrCreateDocument(path);
    }

    public Document loadXml(String xml)
    {
        return XmlUtil.loadXml(xml);
    }

    public void onZapMsgNoHeaders(String zapBody) {
        Log.d(TAG, zapBody);
        try {
            Document doc = loadXml(zapBody);
            if(doc == null)
            {
                Log.d(TAG, "Failed loading xml " + zapBody);
                return;
            }

            XPath xPath = XPathFactory.newInstance().newXPath();
            String expression = "/event/change";
            NodeList changes = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
            if(changes.getLength() == 0)
            {
                expression = "/notify/event/change";
                changes = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
            }

            for (int i = 0; i < changes.getLength(); i++) {
                Node change = changes.item(i);
                String op = XmlUtil.selectSingelNodeText(change, "op");
                String spath = XmlUtil.selectSingelNodeText(change, "spath");
                String rpath = XmlUtil.selectSingelNodeText(change, "rpath");
                Node data = XmlUtil.selectSingelNode(change, "data");

                Log.d(TAG, "spath: " + spath + " rpath: " + rpath + "op: " + op);
                if(op.equals("create") && rpath.isEmpty())
                {
                    subs.remove(spath); // subscription is reinitialized due ot unsubscribe -> subscribe
                }
                Document editDoc = getDocument(spath);
                onChange(editDoc, op, rpath, spath, XmlUtil.selectSingelNode(data, "*[1]"));

                Bundle b = new Bundle();
                String datamessage = XmlUtil.stringifyXml(editDoc);
                b.putString(ZapService.SUB_DATA, datamessage);
                b.putString(ZapService.SUB_SPATH, spath);
                Message m = new Message();
                m.setData(b);
                messenger.send(m);


            }
            expression = "/reply/out";
            NodeList replies = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
            if(replies.getLength() == 0)
            {
                expression = "/out";
                replies = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
            }
            String refid = XmlUtil.selectSingelNodeText(doc, "/reply/ref");
            for (int i = 0; i < replies.getLength(); i++) {

                Bundle b = new Bundle();
                Node rpc = XmlUtil.selectSingelNode(replies.item(i), "*[1]");

                String dmsg = XmlUtil.stringifyNode(rpc);
                b.putString(ZapService.REPLY_DATA, dmsg);
                if(refid != null)
                    b.putString(ZapService.RPC_REFID, refid);
                Message m = new Message();
                m.setData(b);
                messenger.send(m);
            }
        } catch (Exception e) {
            Log.d(TAG, "exception", e);
            return;
        }

    }

    public void onChange(Document subscriptionData, String op, String rpath, String spath, Node data)
    {
        if (op == null || spath == null || rpath == null)
            return;

        if (op.equals("replace") || op.equals("delete")) // replace or delete requires that we delete a node
        {
            Node nodeToDelete = XmlUtil.selectSingelNode(subscriptionData, (getXpathToNode(spath, rpath))); // find node to delete using spath + rpath
            assert nodeToDelete != null;
            Node parent = nodeToDelete.getParentNode(); // get parent node so we can delete this node
            parent.removeChild(nodeToDelete);
        }

        Log.d(TAG, "rpath: " + rpath);

        if ((op.equals("create") || op.equals("replace")) && data != null) // replace or create requires that we create a node
        {
            if (rpath.isEmpty()) // no data has been sent earlier
            {
                Node importNode = subscriptionData.importNode(data, true);
                subscriptionData.appendChild(importNode);
                Log.d(TAG, XmlUtil.stringifyXml(subscriptionData));
            }
            else
            {
                String parentXpath = getXpathToParent(spath, rpath);
                Node dataNode = XmlUtil.selectSingelNode(subscriptionData, parentXpath); // select parent node where we should add received data

                Node importNode = subscriptionData.importNode(data, true);
                assert dataNode != null;
                dataNode.appendChild(importNode);

            }
        }
    }

    String getXpathToParent(String zapspath, String zaprpath)
    {
        String[] strings = zapspath.split("/");
        String spath_use = "/";
        if (strings.length > 1)
            spath_use = spath_use + strings[strings.length - 1];

        String rpath_use = "";

        String[] rpath_split = zaprpath.split("/");
        for (int a = 0; a < rpath_split.length - 1; a++)
        {
            if(rpath_split[a].length() > 0)
                rpath_use = rpath_use + "/" + rpath_split[a];
        }

        return spath_use + rpath_use;
    }

    String getXpathToNode(String zapspath, String zaprpath)
    {
        String[] strings = zapspath.split("/");
        String spath_use = "";
        if (strings.length > 1)
            spath_use = strings[strings.length - 1];
        spath_use = "/" + spath_use;
        return spath_use + zaprpath;
    }

    private void sendConStatus(String status)
    {
        Bundle b = new Bundle();
        b.putString(ZapService.TCP_CON, status);
        Message m = new Message();
        m.setData(b);
        try {
            messenger.send(m);
        } catch (RemoteException e) {
            Log.d(TAG, "Exception", e);
        }
    }

    private void runloop()
    {
        while(mRun)
        {
            try {
                sendConStatus("connecting");
                Log.d(TAG, "Connecting... " + ipNumber);

                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(ipNumber, 50004), 5000);
                bufferedMessage = "";
                try {

                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    sendConStatus("connected");
                    sendMessage("\f\nencode:xml\n\n<request><rpc><get><path>/state/endpoint</path></get></rpc></request>\n\f\n");

                    //Listen for the incoming messages while mRun = true
                    while (mRun) {
                        incomingMessage = in.readLine();
                        if(incomingMessage != null && incomingMessage.contains("\f"))
                        {
                            bufferedMessage = bufferedMessage + incomingMessage;
                            if(bufferedMessage.indexOf("<") > 0)
                            {
                                String data = bufferedMessage.substring(bufferedMessage.indexOf("<"));
                                onZapMsgNoHeaders(data);
                            }
                            incomingMessage = null;
                            bufferedMessage = "";
                        }
                        else if(incomingMessage != null)
                            bufferedMessage = bufferedMessage + incomingMessage;
                    }
                } catch (Exception e) {

                    Log.d(TAG, "Error", e);

                } finally {

                    out.flush();
                    out.close();
                    in.close();
                    socket.close();
                    Log.d(TAG, "Socket Closed");
                }

            } catch (SocketTimeoutException e) {
                Log.d(TAG, "Socket timeout exception");
            } catch (IOException e) {
                Log.d(TAG, "Error", e);
            }

            sendConStatus("disconnected");

            try {
                Thread.sleep(1000*60);
            } catch (InterruptedException e) {

            }
        }
    }

    public void run() {

        mRun = true;
        new Thread(new Runnable() {
            public void run(){
                runloop();
           }
        }).start();
    }

}