package it.jwood.xml.dom;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.Iterator;

/**
 *
 * */
public class ElementListIterator implements Iterator<Element> {
    private NodeList children;
    private int index;
    private Element element;
    private boolean ignore;
    private int limit;

    public ElementListIterator(NodeList children){
        this(children, false, -1);
    }
    public ElementListIterator(NodeList children, int limit){
        this(children, false, limit);
    }
    public ElementListIterator(NodeList children, boolean ignore){
        this(children, ignore, -1);
    }
    public ElementListIterator(NodeList children, boolean ignore, int limit){
        this.children = children;
        this.ignore = ignore;
        if (children == null){
            this.limit = 0;
        }else
            this.limit = (limit >=0 && limit < children.getLength()) ? limit : children.getLength();
    }

    @Override
    public boolean hasNext() {
        if (children == null)
            return false;
        if (element != null)
            return true;
        while (index < limit && element == null){
            element = XMLUtils.toElement(children.item(index++), ignore);
        }
        return element != null;

    }

    @Override
    public Element next() {
        if (!hasNext())
            return null;
        if (element != null){
            Element tmp = element;
            element = null;
            return tmp;
        }
        return null;
    }
}
