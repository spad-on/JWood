package it.jwood.commons.io;

import java.io.IOException;

@FunctionalInterface
public interface IOSupplier<R> {
    R get() throws IOException;
}
