package it.jwood.xml.dom;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class XMLDOMParser {
    private XMLDOMParser(){}


    public static Document load(String inputFile) throws IOException {
        return load(new File(inputFile));
    }

    public static Document load(File inputFile) throws IOException {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            return doc;
        }catch (ParserConfigurationException | SAXException e){
            throw new IOException(e);
        }
    }

    public static Document parse(String xmlDocument) throws IOException {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            ByteArrayInputStream input = new ByteArrayInputStream(xmlDocument.getBytes("UTF-8"));
            Document doc = dBuilder.parse(input);
            return doc;
        }catch (ParserConfigurationException | SAXException e){
            throw new IOException(e);
        }
    }

    public static void save(Document doc, File outputFile) throws IOException {
        save(doc, new StreamResult(outputFile));
    }

    public static void save(Document doc, String outputFile) throws IOException {
        save(doc, new StreamResult(new File(outputFile)));
    }

    public static void save(Document doc, OutputStream os) throws IOException {
        save(doc, new StreamResult(os));
    }

    private static void save(Document doc, StreamResult result) throws IOException {
        // write the content into xml file
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, result);
            if (result.getWriter() != null){
                result.getWriter().close();
            }
        } catch (TransformerException e) {
            throw new IOException(e);
        }
    }


}
