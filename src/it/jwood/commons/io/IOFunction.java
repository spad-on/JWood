package it.jwood.commons.io;

import java.io.IOException;

@FunctionalInterface
public interface IOFunction<T, R> {
    R apply(T arg) throws IOException;
}