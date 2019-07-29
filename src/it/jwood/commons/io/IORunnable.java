package it.jwood.commons.io;

import java.io.IOException;

@FunctionalInterface
public interface IORunnable {
    void run() throws IOException;
}