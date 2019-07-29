package it.jwood.xml.dom;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Iterator;

/**
 *
 * */
public class NodeListIterator implements Iterator<Node> {
    private NodeList children;
    private int index;
    private int limit;

    public NodeListIterator(NodeList children, int limit){
        this.children = children;
        if (children == null){
            this.limit = 0;
        }else
            this.limit = (limit >=0 && limit < children.getLength()) ? limit : children.getLength();
    }


    @Override
    public boolean hasNext() {
        return children != null && index < limit;
    }

    @Override
    public Node next() {
        return children == null ? null : children.item(index++);
    }
}
