package com.evmobile.eversvik.stationmonitor.src.activity;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import com.evmobile.eversvik.stationmonitor.src.config.DeviceConfig;
import com.evmobile.eversvik.stationmonitor.src.device.Calling;
import com.evmobile.eversvik.stationmonitor.src.device.DeviceResource;
import com.evmobile.eversvik.stationmonitor.src.device.Gpis;
import com.evmobile.eversvik.stationmonitor.src.device.Gpos;
import com.evmobile.eversvik.stationmonitor.src.device.ZapSubscription;
import com.evmobile.eversvik.stationmonitor.src.event.EventRule;
import com.evmobile.eversvik.stationmonitor.src.config.EventRuleConfig;
import com.evmobile.eversvik.stationmonitor.src.device.ZapDataIface;
import com.evmobile.eversvik.stationmonitor.src.device.ZapRpcIface;
import com.evmobile.eversvik.stationmonitor.src.util.XmlUtil;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by eversvik on 12.02.2016.
 */
public class ZapDevice implements ZapDataIface, DeviceResource {

    @Override
    public void sendZapRpc(ZapRpcIface data) {
        Log.d("TCP", data.toXmlString());
        if(tcpCon != null)
            tcpCon.sendMessage(data.toXmlString()+"\n\f\n");
    }

    Calling calling;
    public Calling getCalling()
    {
        return calling;
    }

    Gpos gpos;
    public Gpos getGpos() { return gpos; }

    Gpis gpis;
    public Gpis getGpis() { return gpis; }

    @Override
    public String getId() {
        return id;
    }


    public interface OnEndpointUpdated {
        public void onSubscribeUpdated(String id, String sub, String data);
        public void onTcpUpdated(String id, String status);
        public void onReplyData(String id, String reply_data);
    }

    String id;
    String ipaddress;
    ZapTcpCon tcpCon = null;
    OnEndpointUpdated cb = null;


    DeviceConfig config;
    ArrayList<EventRule> rule = new ArrayList<>();

    public void createRule(EventRuleConfig ruleconf)
    {
        config.addRule(ruleconf);
        EventRule rule = new EventRule(ruleconf, this);
        this.rule.add(rule);
    }

    public void removeRule(EventRule rule)
    {
        config.removeRule(rule.getConfig());
        this.rule.remove(rule);
    }

    public ArrayList<EventRule> getRules() {
        return rule;
    }

    public ZapDevice(OnEndpointUpdated cb, DeviceConfig config)
    {
        this.cb = cb;
        this.config = config;
        this.ipaddress = config.getIpaddress();
        this.id = config.getName();

        calling = new Calling(this);
        gpos = new Gpos(this);
        gpis = new Gpis(this);

    }

    public void init()
    {
        for(int i = 0; i < config.getRules().size(); i++)
        {
            EventRule rule = new EventRule(config.getRules().get(i), this);
            this.rule.add(rule);
        }

    }

    public void sendTcpData(String data)
    {
        if(tcpCon != null)
            tcpCon.sendMessage(data+"\n\f\n");
    }

    public void createTcp()
    {
        if(tcpCon == null)
        {
            Messenger mesenger = new Messenger(new TcpHandler(Looper.getMainLooper()));

            tcpCon = new ZapTcpCon(mesenger, ipaddress, id);
            tcpCon.run();
        }
    }

    public void freeResources()
    {
        tcpCon.stopClient();
    }

    HashMap<String,String> subs = new HashMap<String,String>();
    String tcpStatus = "disconnected";

    ArrayList<ZapSubscription> zapSub = new ArrayList<>();

    @Override
    public void addSubscribe(ZapSubscription sub) {
        if(!zapSub.contains(sub))
        {
            zapSub.add(sub);
            sendTcpData(sub.subString());
        }
    }

    @Override
    public void removeSubscribe(ZapSubscription sub) {
        if(zapSub.contains(sub))
        {
            zapSub.remove(sub);
            sendTcpData(sub.unsubString());
        }
    }

    private void subscriptionUpdated(String spath, String data)
    {
        for(int i = 0; i < zapSub.size(); i++)
        {
            ZapSubscription s = zapSub.get(i);
            if(s.getPath().equals(spath))
            {
                s.onDataChanged(data);
            }
        }
    }

    private void onReply(String data, String refid)
    {
        if(refid != null)
        {
            for(int i = 0; i < zapSub.size(); i++)
            {
                ZapSubscription s = zapSub.get(i);
                if(s.getRefId().equals(refid))
                {
                    Document doc = XmlUtil.loadXml(data);
                    s.setSubId(XmlUtil.selectSingelNodeText(doc, "/subscribe/subId"));
                }
            }
        }
    }

    private void connected()
    {
        System.out.println("Connected sub sending");
        for(int i = 0; i < zapSub.size(); i++) {
            ZapSubscription s = zapSub.get(i);
            sendTcpData(s.subString());
        }
    }

    class TcpHandler extends Handler {
        public TcpHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            String spath = msg.getData().getString(ZapService.SUB_SPATH);
            String subdata = msg.getData().getString(ZapService.SUB_DATA);
            if(spath != null && subdata != null)
            {
                subscriptionUpdated(spath, subdata);
                subs.put(spath, subdata);
                if(cb != null) {
                    cb.onSubscribeUpdated(id, spath, subdata);
                }
            }

            if(msg.getData().getString(ZapService.REPLY_DATA) != null)
            {
                onReply(msg.getData().getString(ZapService.REPLY_DATA), msg.getData().getString(ZapService.RPC_REFID));
                if(cb != null)
                    cb.onReplyData(id, msg.getData().getString(ZapService.REPLY_DATA));
            }

            if(msg.getData().getString(ZapService.TCP_CON) != null)
            {
                tcpStatus = msg.getData().getString(ZapService.TCP_CON);
                assert(tcpStatus != null);
                if(tcpStatus.equals("connected"))
                    connected();

                if(cb != null)
                    cb.onTcpUpdated(id, msg.getData().getString(ZapService.TCP_CON));
            }
        }
    }
}
