package it.jwood.xml.dom;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.function.Consumer;

public class XMLStream {

    public static XMLStream stream(Node node){
        return new XMLStream(node, null);
    }

    private HNode current;
    private int limit;

    private XMLStream(Node node, HNode parent){
        this.current = new HNode(node, parent);
        this.limit = -1;
    }

    private void ensureNode(){
        if (current == null){
            throw new XMLException("Stream node is null");
        }
    }

    public XMLStream limit(int limit){
        this.limit = limit;
        return this;
    }

    public XMLStream up(){
        this.current = current.parent;
        ensureNode();
        return this;
    }

    public XMLStream down(String tag){
        Element element = XMLUtils.toElement(current.node);
        NodeList children = element.getElementsByTagName(tag);
        if (children == null || children.getLength() != 1){
            throw new XMLException("'" + tag + "' is not only child: " + (children != null ? children.getLength() : "null" ));
        }
        this.current = new HNode(children.item(0), current);
        return this;
    }

    public XMLStream down(){
        NodeList children = current.node.getChildNodes();
        if (children == null || children.getLength() != 1){
            throw new XMLException("Not unique child: " + (children != null ? children.getLength() : "null" ));
        }
        this.current = new HNode(children.item(0), current);
        return this;
    }

    public XMLStream collect(Consumer<Element> action){
        action.accept(XMLUtils.toElement(current.node));
        return this;
    }

    public XMLStream iter(String tag, Consumer<Node> action){
        Element element = XMLUtils.toElement(current.node);
        NodeList children = element.getElementsByTagName(tag);
        if (children == null)
            return this;
        int end = (limit >= 0 && limit < children.getLength()) ? limit : children.getLength();
        for (int i = 0; i < end; i++){
            action.accept(children.item(i));
        }
        return this;
    }

    public XMLStream iter(Consumer<Node> action){
        NodeList children = current.node.getChildNodes();
        if (children == null)
            return this;
        int end = (limit >= 0 && limit < children.getLength()) ? limit : children.getLength();
        for (int i = 0; i < end; i++){
            action.accept(children.item(i));
        }
        return this;
    }

    public XMLStream iterElement(String tag, Consumer<Element> action){
        Element element = XMLUtils.toElement(current.node);
        NodeList children = element.getElementsByTagName(tag);
        if (children == null)
            return this;
        int end = (limit >= 0 && limit < children.getLength()) ? limit : children.getLength();
        for (int i = 0; i < end; i++){
            Element eChild = XMLUtils.toElement(children.item(i));
            action.accept(eChild);
        }
        return this;
    }

    public XMLStream iterElement(Consumer<Element> action){
        NodeList children = current.node.getChildNodes();
        if (children == null)
            return this;
        int end = (limit >= 0 && limit < children.getLength()) ? limit : children.getLength();
        for (int i = 0; i < end; i++){
            Element eChild = XMLUtils.toElement(children.item(i), true);
            if (eChild != null)
                action.accept(eChild);
        }
        return this;
    }

    public XMLStream subStream(String tag, Consumer<XMLStream> action){
        Element element = XMLUtils.toElement(current.node);
        NodeList children = element.getElementsByTagName(tag);
        if (children == null)
            return this;
        int end = (limit >= 0 && limit < children.getLength()) ? limit : children.getLength();
        for (int i = 0; i < end; i++){
            action.accept(new XMLStream(children.item(i), current));
        }
        return this;
    }

    public XMLStream subStream(Consumer<XMLStream> action){
        NodeList children = current.node.getChildNodes();
        if (children == null)
            return this;
        int end = (limit >= 0 && limit < children.getLength()) ? limit : children.getLength();
        for (int i = 0; i < end; i++){
            action.accept(new XMLStream(children.item(i), current));
        }
        return this;
    }

    public Iterable<Node> iter(String tag){
        Element element = XMLUtils.toElement(current.node);
        NodeList children = element.getElementsByTagName(tag);
        return () -> new NodeListIterator(children, limit);
    }

    public Iterable<Element> iterElement(String tag){
        Element element = XMLUtils.toElement(current.node);
        NodeList children = element.getElementsByTagName(tag);
        return () -> new ElementListIterator(children, limit);
    }

    public Iterable<Node> iter(){
        NodeList children = current.node.getChildNodes();
        return () -> new NodeListIterator(children, limit);
    }

    public Iterable<Element> iterElement(){
        NodeList children = current.node.getChildNodes();
        return () -> new ElementListIterator(children, true, limit);
    }

    public Element toElement(){
        return XMLUtils.toElement(current.node);
    }

    /**
     *
     * */
    private static class HNode {
        protected HNode parent;
        protected  Node node;

        public HNode(Node current, HNode parent){
            this.node = current;
            this.parent = parent;
        }

        public Node getNode(){
            return node;
        }

        public HNode getParent(){
            return parent;
        }
    }



}
