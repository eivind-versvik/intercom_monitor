package com.evmobile.eversvik.stationmonitor.src.device;

import android.support.annotation.NonNull;

import com.evmobile.eversvik.stationmonitor.src.util.XmlUtil;
import com.evmobile.eversvik.stationmonitor.src.event.util.EventObservable;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Observer;

/**
 * Created by eversvik on 15.04.2016.
 * Zap call handling - handles multiple calls, parsing of ZAP data
 */
public class Calling {

    ArrayList<Call> calls = new ArrayList<>();
    EventObservable obs = new EventObservable();
    private ZapDataIface dataIface;
    ZapSubscription sub;

    public Calling(ZapDataIface dataIface) {
        this.dataIface = dataIface;
        sub = new ZapSubscription("/state/calling", new Sub());
    }

    public ArrayList<Call> getCalls() {
        return calls;
    }

    public void addObserver(@NonNull Observer observer) {
        if (obs.countObservers() == 0) {
            this.dataIface.addSubscribe(sub);
        }
        obs.addObserver(observer);
    }

    public void removeObserver(@NonNull Observer observer) {
        boolean updateedIfDeleted = false;

        if (obs.countObservers() == 1)
            updateedIfDeleted = true;

        obs.deleteObserver(observer);

        if (obs.countObservers() == 0 && updateedIfDeleted)
            this.dataIface.removeSubscribe(sub);
    }

    class Sub implements ZapSubscription.ZapSubscriber {
        @Override
        public void onDataChanged(String data) {
            onCallChanged(data);
        }
    }

    private void onCallChanged(String calling) {
        Document callingXml = XmlUtil.loadXml(calling);
        if (callingXml == null) {
            return;
        }

        NodeList callList = XmlUtil.selectNodes(callingXml.getDocumentElement(), "/calling/call");
        if (callList == null)
            return;

        clearUnknownCalls(callList); // Removes all deleted calls from local list

        for (int i = 0; i < callList.getLength(); i++) {

            Node n = callList.item(i);
            String call_id = XmlUtil.selectSingelNodeText(n, "call_id");
            Call c = getCall(call_id);
            if (c == null) {
                c = new Call(call_id);
                calls.add(c);
            }
            c.setUsername(XmlUtil.selectSingelNodeText(n, "username"));
            c.setDisplay(XmlUtil.selectSingelNodeText(n, "display"));
            c.setState(XmlUtil.selectSingelNodeText(n, "state"));
        }

        obs.setChanged();
        obs.notifyObservers();
    }

    private Call getCall(String call_id) {
        for (int i = 0; i < calls.size(); i++) {
            Call c = calls.get(i);
            if (c.getCallId().equals(call_id))
                return c;
        }
        return null;
    }

    private void removeCall(String call_id) {
        for (int i = 0; i < calls.size(); i++) {
            Call c = calls.get(i);
            if (c.getCallId().equals(call_id)) {
                calls.remove(c);
                return;
            }
        }
    }

    private boolean itemExists(NodeList callList, String call_id) {
        for (int i = 0; i < callList.getLength(); i++) {
            Node n = callList.item(i);
            String call_id_xml = XmlUtil.selectSingelNodeText(n, "call_id");
            if (call_id.equals(call_id_xml))
                return true;
        }
        return false;
    }

    private void clearUnknownCalls(NodeList callList) {
        ArrayList<String> ids = new ArrayList<>();
        for (int i = 0; i < calls.size(); i++) {
            Call c = calls.get(i);
            if (!itemExists(callList, c.getCallId())) {
                ids.add(c.getCallId());
            }
        }
        for (int i = 0; i < ids.size(); i++) {
            removeCall(ids.get(i));
        }
    }

    public void setupCall(String username)
    {
        dataIface.sendZapRpc(new CallSetup(username));
    }

    public void hangupCall(String call_id)
    {
        dataIface.sendZapRpc(new CallHangup(call_id));
    }

    public class CallSetup implements ZapRpcIface {

        private String username;

        public CallSetup(String username)
        {
            this.username = username;
        }

        @Override
        public String toXmlString() {
            return "<rpc><call_setup><to>" + username + "</to></call_setup></rpc>";
        }
    }

    public class CallHangup implements ZapRpcIface {

        private String call_id;

        public CallHangup(String call_id)
        {
            this.call_id = call_id;
        }

        @Override
        public String toXmlString() {
            return "<rpc><call_ctrl><action>hangup</action><call_id>" + call_id + "</call_id></call_ctrl></rpc>";
        }
    }
}
