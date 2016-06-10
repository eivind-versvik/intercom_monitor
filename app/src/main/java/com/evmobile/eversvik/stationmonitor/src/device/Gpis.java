package com.evmobile.eversvik.stationmonitor.src.device;

import com.evmobile.eversvik.stationmonitor.src.util.XmlUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;

/**
 * Created by eversvik on 13.05.2016.
 * Implements montioring of GPIs
 */
public class Gpis {
    private ZapDataIface dataIface;
    private ZapSubscription sub;
    private HashMap<String, Gpi> list = new HashMap<>();

    public Gpis(ZapDataIface dataIface) {
        this.dataIface = dataIface;
        sub = new ZapSubscription("/state/gpis", new Sub());
        this.dataIface.addSubscribe(sub);
    }

    public Gpi createOrGetGpi(String id)
    {
        Gpi gpi = this.list.get(id);
        if(gpi == null)
        {
            gpi = new Gpi(dataIface, id);
            this.list.put(id, gpi);
        }
        return gpi;
    }

    void onGpiChanged(String data)
    {
        Document d = XmlUtil.loadXml(data);
        NodeList list = XmlUtil.selectNodes(d, "/gpis/gpi");
        for(int i = 0; i < list.getLength(); i++)
        {
            Node gpiN = list.item(i);
            if(XmlUtil.selectSingelNodeText(gpiN, "active").isEmpty())
                continue;
            String id = XmlUtil.selectSingelNodeText(gpiN, "kid");
            Gpi gpi = createOrGetGpi(id);
            gpi.setState(Gpi.textToState(XmlUtil.selectSingelNodeText(gpiN, "active")));
        }
    }

    public HashMap<String,Gpi> getGpiList() {
        return list;
    }

    class Sub implements ZapSubscription.ZapSubscriber {
        @Override
        public void onDataChanged(String data) {
            onGpiChanged(data);
        }
    }
}

