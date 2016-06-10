package com.evmobile.eversvik.stationmonitor.src.device;

import com.evmobile.eversvik.stationmonitor.src.util.XmlUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;

/**
 * Created by eversvik on 22.04.2016.
 * ZAP gpoList handling, handles multiple gpoList, parsing of ZAP data
 */
public class Gpos {
    private ZapDataIface dataIface;
    private ZapSubscription sub;
    private HashMap<String, Gpo> gpoList = new HashMap<>();

    public Gpos(ZapDataIface dataIface) {
        this.dataIface = dataIface;
        sub = new ZapSubscription("/state/gpos", new Sub());
        this.dataIface.addSubscribe(sub);
    }

    public Gpo createOrGetGpo(String id)
    {
        Gpo gpo = this.gpoList.get(id);
        if(gpo == null)
        {
            gpo = new Gpo(dataIface, id);
            this.gpoList.put(id, gpo);
        }
        return gpo;
    }

    void onGpoChanged(String data)
    {
        Document d = XmlUtil.loadXml(data);
        NodeList list = XmlUtil.selectNodes(d, "/gpos/gpo");
        for(int i = 0; i < list.getLength(); i++)
        {
            Node gpoN = list.item(i);
            if(XmlUtil.selectSingelNodeText(gpoN, "state").isEmpty())
                continue;
            String id = XmlUtil.selectSingelNodeText(gpoN, "kid");
            Gpo gpo = createOrGetGpo(id);
            gpo.setState(Gpo.textToState(XmlUtil.selectSingelNodeText(gpoN, "state")));
        }
    }

    public HashMap<String,Gpo> getGpoList() {
        return gpoList;
    }

    class Sub implements ZapSubscription.ZapSubscriber {
        @Override
        public void onDataChanged(String data) {
            onGpoChanged(data);
        }
    }
}
