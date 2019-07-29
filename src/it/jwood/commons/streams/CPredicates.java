package it.jwood.commons.streams;

import it.jwood.commons.tuples.Pair;
import it.jwood.commons.tuples.Triplet;

import java.util.Comparator;
import java.util.Objects;

public class CPredicates {
    private CPredicates(){}
    
    
    public static <T> boolean keepLessEqual(Pair<? extends T, ? extends T> pair, Comparator<? super T> cmp) {
        Objects.requireNonNull(cmp);
        return cmp.compare(pair.getFirst(), pair.getSecond()) <= 0;
    }
    
    public static <T> boolean keepLess(Pair<? extends T, ? extends T> pair, Comparator<? super T> cmp) {
        Objects.requireNonNull(cmp);
        return cmp.compare(pair.getFirst(), pair.getSecond()) < 0;
    }
    
    public static <T> boolean keepGreater(Pair<? extends T, ? extends T> pair, Comparator<? super T> cmp) {
        Objects.requireNonNull(cmp);
        return cmp.compare(pair.getFirst(), pair.getSecond()) > 0;
    }
    
    public static <T> boolean keepGreaterEqual(Pair<? extends T, ? extends T> pair, Comparator<? super T> cmp) {
        Objects.requireNonNull(cmp);
        return cmp.compare(pair.getFirst(), pair.getSecond()) >= 0;
    }

    @SuppressWarnings("unchecked")
    public static <T> boolean keepLessEqual(Pair<? extends T, ? extends T> pair) {
        return ((Comparable<? super T>)pair.getFirst()).compareTo(pair.getSecond()) <= 0;
    }
    
    @SuppressWarnings("unchecked")
    public static <T> boolean keepLess(Pair<? extends T, ? extends T> pair) {
        return ((Comparable<? super T>)pair.getFirst()).compareTo(pair.getSecond()) < 0;
    }

    @SuppressWarnings("unchecked")
    public static <T> boolean keepGreater(Pair<? extends T, ? extends T> pair) {
        return ((Comparable<? super T>)pair.getFirst()).compareTo(pair.getSecond()) > 0;
    }

    @SuppressWarnings("unchecked")
    public static <T> boolean keepGreaterEqual(Pair<? extends T, ? extends T> pair) {
        return ((Comparable<? super T>)pair.getFirst()).compareTo(pair.getSecond()) >= 0;
    }
    
    
    public static <T> boolean keepLessEqual(Triplet<? extends T, ? extends T, ?> triplet, Comparator<? super T> cmp) {
        Objects.requireNonNull(cmp);
        return cmp.compare(triplet.getFirst(), triplet.getSecond()) <= 0;
    }
    
    public static <T> boolean keepLess(Triplet<? extends T, ? extends T, ?> triplet, Comparator<? super T> cmp) {
        Objects.requireNonNull(cmp);
        return cmp.compare(triplet.getFirst(), triplet.getSecond()) < 0;
    }
    
    public static <T> boolean keepGreater(Triplet<? extends T, ? extends T, ?> triplet, Comparator<? super T> cmp) {
        Objects.requireNonNull(cmp);
        return cmp.compare(triplet.getFirst(), triplet.getSecond()) > 0;
    }
    
    public static <T> boolean keepGreaterEqual(Triplet<? extends T, ? extends T, ?> triplet, Comparator<? super T> cmp) {
        Objects.requireNonNull(cmp);
        return cmp.compare(triplet.getFirst(), triplet.getSecond()) >= 0;
    }

    @SuppressWarnings("unchecked")
    public static <T> boolean keepLessEqual(Triplet<? extends T, ? extends T, ?> triplet) {
        return ((Comparable<? super T>)triplet.getFirst()).compareTo(triplet.getSecond()) <= 0;
    }

    @SuppressWarnings("unchecked")
    public static <T> boolean keepLess(Triplet<? extends T, ? extends T, ?> triplet) {
        return ((Comparable<? super T>)triplet.getFirst()).compareTo(triplet.getSecond()) < 0;
    }

    @SuppressWarnings("unchecked")
    public static <T> boolean keepGreater(Triplet<? extends T, ? extends T, ?> triplet) {
        return ((Comparable<? super T>)triplet.getFirst()).compareTo(triplet.getSecond()) > 0;
    }

    @SuppressWarnings("unchecked")
    public static <T> boolean keepGreaterEqual(Triplet<? extends T, ? extends T, ?> triplet) {
        return ((Comparable<? super T>)triplet.getFirst()).compareTo(triplet.getSecond()) >= 0;
    }
    
    
    
}
