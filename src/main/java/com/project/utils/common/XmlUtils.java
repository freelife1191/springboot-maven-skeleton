package com.project.utils.common;

import com.google.common.collect.Lists;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by KMS on 03/06/2020.
 */
public class XmlUtils {
    public static class XmlElement {
        private String name;
        private String value;
        private LinkedHashMap<String, XmlAttribute> attributeMap = new LinkedHashMap<>();
        private LinkedHashMap<String, List<XmlElement>> elementMap = new LinkedHashMap<>();
        private List<XmlElement> elementList = Lists.newArrayList();

        public XmlElement(String name) { this.setName(name); }

        public XmlElement(String name, String value) {
            this.setName(name);
            this.setValue(value);
        }

        public XmlAttribute attr(String key, String value) {
            XmlAttribute e = new XmlAttribute(key, value);
            attributeMap.put(e.getKey(), e);
            return e;
        }

        public XmlElement append(XmlElement xmlElement) {
            elementList.add(xmlElement);
            String key = xmlElement.getName();
            if (elementMap.containsKey(key)) { elementMap.get(key).add(xmlElement); } else {
                elementMap.put(key, Lists.newArrayList(xmlElement));
            }
            return xmlElement;
        }

        public XmlElement append(String name, String value) {
            XmlElement e = new XmlElement(name, value);
            append(e);
            return e;
        }

        public XmlElement append(String name) {
            XmlElement e = new XmlElement(name);
            append(e);
            return e;
        }

        public void remove(String name) {
            elementMap.remove(name);
            for (int i = 0; i < elementList.size(); i++) {
                XmlElement element = elementList.get(i);
                if ("name".equals(element.getName())) {
                    elementList.remove(i);
                    i--;
                }
            }
        }

        public void clear() {
            elementMap.clear();
            elementList.clear();
        }

        public String getName() { return name; }

        public void setName(String name) { this.name = name; }

        public String getValue() { return value; }

        public void setValue(String value) { this.value = value; }

        public List<XmlElement> getElementList() { return elementList; }

        public List<XmlAttribute> getAttributeList() { return new ArrayList<XmlAttribute>(attributeMap.values()); }

        public XmlElement getElement(String name) {
            List<XmlElement> list = elementMap.get(name);
            return list == null || list.size() == 0 ? null : list.get(0);
        }

        public List<XmlElement> getElementList(String name) { return elementMap.get(name); }
    }

    public static class XmlAttribute {
        private String key;
        private String value;

        public XmlAttribute(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() { return key; }

        public String getValue() { return value; }
    }

    public static String toString(XmlElement xmlElement) throws ParserConfigurationException, TransformerException { return toString(toNode(xmlElement)); }

    public static String toString(Node n) throws TransformerException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transformer = transFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(n);
        StreamResult result = new StreamResult(out);
        transformer.transform(source, result);
        String xml = new String(out.toByteArray(), StandardCharsets.UTF_8);
        Document document = n.getOwnerDocument();
        if (document != null) { xml = xml.replaceFirst("<\\?xml[^>]+>", ""); }
        return xml;
    }

    public static String toString(NodeList nodeList) throws TransformerException {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < nodeList.getLength(); i++) { sb.append(toString(nodeList.item(i))); }
        return sb.toString();
    }

    public static Element toElement(Document document, XmlElement xmlElement) {
        Element element = document.createElement(xmlElement.getName());
        for (XmlAttribute attr : xmlElement.getAttributeList()) {
            element.setAttribute(attr.getKey(), attr.getValue());
        }
        element.setTextContent(xmlElement.getValue());
        for (XmlElement child : xmlElement.getElementList()) { element.appendChild(toElement(document, child)); }
        return element;
    }

    public static Node toNode(XmlElement xmlElement) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        document.setXmlStandalone(true);
        document.appendChild(toElement(document, xmlElement));
        Element element = document.createElement(xmlElement.getName());
        for (XmlAttribute attr : xmlElement.getAttributeList()) {
            element.setAttribute(attr.getKey(), attr.getValue());
        }
        return document;
    }

    public static XmlElement parse(String xml) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        InputSource inputSource = new InputSource();
        inputSource.setCharacterStream(new StringReader(xml));
        Document document = documentBuilder.parse(inputSource);
        Element documentElement = document.getDocumentElement();
        return toXmlElement(documentElement);
    }

    public static XmlElement toXmlElement(Node node) {
        XmlElement xmlElement = new XmlElement(node.getNodeName(), node.getNodeValue());
        NamedNodeMap attributes = node.getAttributes();
        if (attributes != null) {
            for (int i = 0; i < attributes.getLength(); i++) {
                Node item = attributes.item(i);
                xmlElement.attr(item.getNodeName(), item.getNodeValue());
            }
        }
        NodeList childNodes = node.getChildNodes();
        if (childNodes != null) {
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node item = childNodes.item(i);
                switch (item.getNodeType()) {
                    case Node.TEXT_NODE:
                    case Node.CDATA_SECTION_NODE:
                        xmlElement.setValue(item.getNodeValue());
                        break;
                    case Node.ELEMENT_NODE:
                        xmlElement.append(toXmlElement(item));
                        break;
                }
            }
        }
        return xmlElement;
    }

}
