package it.jwood.commons.io;

import java.io.IOException;

@FunctionalInterface
public interface IOConsumer<T> {
    void apply(T arg) throws IOException;
}