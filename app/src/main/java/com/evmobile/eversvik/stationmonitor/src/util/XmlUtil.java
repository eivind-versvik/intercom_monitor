package com.evmobile.eversvik.stationmonitor.src.util;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

/**
 * Created by eversvik on 13.02.2016.
 */
public class XmlUtil {
    static public String stringifyNode(Node doc)
    {
        try{
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
            return output;
        }
        catch(Exception e)
        {
            return "";
        }
    }

    static public String stringifyXml(Document doc)
    {
        try{
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
            return output;
        }
        catch(Exception e)
        {
            return null;
        }
    }

    static public NodeList selectNodes(Node node, String xpath)
    {
        XPath xPath =  XPathFactory.newInstance().newXPath();
        String expression = xpath;
        try
        {
            return (NodeList) xPath.compile(xpath).evaluate(node, XPathConstants.NODESET);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }


    static public Node selectSingelNode(Node node, String xpath)
    {
        XPath xPath =  XPathFactory.newInstance().newXPath();
        String expression = "/event/change";
        try
        {
            return (Node) xPath.compile(xpath).evaluate(node, XPathConstants.NODE);

        }
        catch(Exception e)
        {
            return null;
        }
    }

    static public void setNodeText(Node node, String xpath, String text)
    {
        XPath xPath =  XPathFactory.newInstance().newXPath();
        String expression = "/event/change";
        try
        {
            Node n = (Node) xPath.compile(xpath).evaluate(node, XPathConstants.NODE);
            n.setTextContent(text);

        }
        catch(Exception e)
        {
            return;
        }
    }

    static public String selectSingelNodeText(Node node, String xpath)
    {
        XPath xPath =  XPathFactory.newInstance().newXPath();
        try
        {
            return ((Node) xPath.compile(xpath).evaluate(node, XPathConstants.NODE)).getTextContent();
        }
        catch(Exception e)
        {
            return "";
        }
    }

    static public Document loadXml(String xml)
    {
        Document ret;
        DocumentBuilder dBuilder;
        DocumentBuilderFactory dbFactory
                = DocumentBuilderFactory.newInstance();
        try {

            dBuilder = dbFactory.newDocumentBuilder();
            ret = dBuilder.newDocument();
            StringBuilder xmlStringBuilder = new StringBuilder();
            xmlStringBuilder.append("<?xml version=\"1.0\"?>");
            xmlStringBuilder.append(xml);

            ByteArrayInputStream input = null;
            input = new ByteArrayInputStream(xmlStringBuilder.toString().getBytes("UTF-8"));
            Document doc = dBuilder.parse(input);
            return doc;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Log.d("testservice", "loadxml" + e.toString());
            return null;
        }
    }
}
