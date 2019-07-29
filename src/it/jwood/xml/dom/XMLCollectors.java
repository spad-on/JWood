package it.jwood.xml.dom;

import org.w3c.dom.Element;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collector;

public class XMLCollectors {


    public static <T> Consumer<Element> attribute(String name, Function<String, T> mapper, Consumer<T> collector){
        return (Element e) -> {
            String val = e.getAttribute(name);
            collector.accept(mapper.apply(val));
        };
    }

    public static Consumer<Element> attribute(String name, Consumer<String> collector){
        return (Element e) -> {
            String val = e.getAttribute(name);
            collector.accept(val);
        };
    }

    public static <T> Consumer<Element> text(Function<String, T> mapper, Consumer<T> collector){
        return (Element e) -> {
            String val = e.getTextContent();
            collector.accept(mapper.apply(val));
        };
    }

    public static Consumer<Element> text(Consumer<String> collector){
        return (Element e) -> {
            String val = e.getTextContent();
            collector.accept(val);
        };
    }

    public static <T> Function<Element, T> attribute(String name, Function<String, T> mapper){
        return (Element e) -> {
            String val = e.getAttribute(name);
            return mapper.apply(val);
        };
    }

    public static Function<Element, String> attribute(String name){
        return (Element e) -> {
            String val = e.getAttribute(name);
            return val;
        };
    }

    public static <T> Function<Element, T> text(Function<String, T> mapper){
        return (Element e) -> {
            String val = e.getTextContent();
            return mapper.apply(val);
        };
    }

    public static Function<Element, String> text(){
        return (Element e) -> {
            String val = e.getTextContent();
            return val;
        };
    }

    public static Collector<Element, ?, String> joining(Function<Element, String> mapping, String delimiter, String start, String end){
        return Collector.of(StringBuilder::new,
                (s, e) -> {if (s.length() > 0) s.append(delimiter); s.append(mapping.apply(e));},
                (s1, s2) -> s1.append(delimiter).append(s2),
                s -> start + s + end);
    }

    public static Collector<Element, ?, String> joining(Function<Element, String> mapping, String delimiter){
        return joining(mapping, delimiter, "", "");
    }


}
