package it.jwood.xml.dom;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class XMLUtils {
    private XMLUtils(){}

    public static Element toElement(Node node){
        return toElement(node, false);
    }
    public static Element toElement(Node node, boolean ignore){
        if (node.getNodeType() != Node.ELEMENT_NODE) {
            if (ignore)
                return null;
            throw new XMLException("Node is not an element: " + node.getNodeType());
        }
        return (Element) node;
    }

    public static List<Node> toList(NodeList nodeList){
        if (nodeList == null)
            return null;
        List<Node> lst = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++){
            lst.add(nodeList.item(i));
        }
        return lst;
    }

    public static List<Element> toElementList(NodeList nodeList){
        if (nodeList == null)
            return null;
        List<Element> lst = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++){
            lst.add(toElement(nodeList.item(i)));
        }
        return lst;
    }


}
