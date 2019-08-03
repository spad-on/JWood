package it.jwood.commons.streams;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CStream {
    private CStream(){}

    public static <T> Function<T, Stream<T>> symmetric(UnaryOperator<T> inverter) {
        return   el -> Stream.of(el, inverter.apply(el));
    }

    public static <T> Stream<T> from(Iterable<T> it){
        return StreamSupport.stream(it.spliterator(), false);
    }

    public static <T> Stream<T> from(Iterator<T> it){
        Iterable<T> iterable = () -> it;
        return from(iterable);
    }


}
