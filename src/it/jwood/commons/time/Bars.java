package it.jwood.commons.time;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Bars {
    private Bars(){}

    public static void setMaxLength(int maxLength){
        ProgressBarBounded.setMaxLength(maxLength);
    }

    public static void setTickProgression(int tickProgression){
        ProgressBarUnbounded.setTickProgression(tickProgression);
    }

    public static <T> Stream<T> wrap(Stream<T> stream, int total){
        ProgressBar bar = bounded(total);
        return stream.onClose(bar::close).peek(s -> bar.update());
    }

    public static <T> Stream<T> wrap(Stream<T> stream){
        ProgressBar bar = unbounded();
        return stream.onClose(bar::close).peek(s -> bar.update());
    }

    public static <T> Iterable<T> wrap(Collection<T> collection){
        return wrap(collection.iterator(), collection.size());
    }

    public static <T> Iterable<T> wrap(Iterator<T> iterator, int total){
        return () -> new ProgressBarBounded.LogIterator<>(iterator, total);
    }

    public static <T> Iterable<T> wrap(Iterable<T> iterable){
        return wrap(iterable.iterator());
    }

    public static <T> Iterable<T> wrap(Iterator<T> iterator){
        return () -> new ProgressBarUnbounded.LogIteratorUnbounded<>(iterator);
    }

    public static IntStream wrap(int[] array){
        ProgressBar bar = bounded(array.length);
        return Arrays.stream(array).onClose(bar::close).peek(s -> bar.update());
    }

    public static LongStream wrap(long[] array){
        ProgressBar bar = bounded(array.length);
        return Arrays.stream(array).onClose(bar::close).peek(s -> bar.update());
    }

    public static DoubleStream wrap(double[] array){
        ProgressBar bar = bounded(array.length);
        return Arrays.stream(array).onClose(bar::close).peek(s -> bar.update());
    }

    public static <T> Stream wrap(T[] array){
        ProgressBar bar = bounded(array.length);
        return Arrays.stream(array).onClose(bar::close).peek(s -> bar.update());
    }

    public static ProgressBar bounded(int total){
        return new ProgressBarBounded(total);
    }

    public static ProgressBar unbounded(int progressionStep){
        return new ProgressBarUnbounded(progressionStep);
    }

    public static ProgressBar unbounded(){
        return new ProgressBarUnbounded();
    }

}
