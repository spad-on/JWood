package it.jwood.commons.streams;

import it.jwood.commons.counters.DoubleCounter;
import it.jwood.commons.counters.FloatCounter;
import it.jwood.commons.counters.IntegerCounter;
import it.jwood.commons.counters.LongCounter;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class CCollectors {

    public static <T> Collector<T, ?, List<T>> limitingList(int limit) {
        return limiting(limit, ArrayList::new);
    }

    public static <T> Collector<T, ?, Set<T>> limitingSet(int limit) {
        return limiting(limit, HashSet::new);
    }

    public static <T, R extends Collection<T>> Collector<T, ?, R> limiting(int limit, Supplier<R> supplier) {
        return Collector.of(
                supplier,
                (l, e) -> { if (l.size() < limit) l.add(e); },
                (l1, l2) -> {
                    for (T el : l2){
                        if (l1.size() >= limit)
                            break;
                        l1.add(el);
                    }
                    return l1;
                }
        );
    }

    public static <T, R> Collector<T, ?, List<T>> lastN(int n) {
        return lastN(n, ArrayList::new);
    }

    public static <T, R extends Collection<T>> Collector<T, ?, R> lastN(int n, Supplier<R> supplier) {
        return Collector.<T, Deque<T>, R>of(ArrayDeque::new, (acc, t) -> {
            if(acc.size() == n)
                acc.pollFirst();
            acc.add(t);
        }, (acc1, acc2) -> {
            while(acc2.size() < n && !acc1.isEmpty()) {
                acc2.addFirst(acc1.pollLast());
            }
            return acc2;
        }, s -> {R lst = supplier.get(); lst.addAll(s); return lst;});
    }

    public static <K> Collector<K, IntegerCounter<K>, IntegerCounter<K>> toIntegerCounter(){
        return Collector.of(IntegerCounter::new, IntegerCounter::increment, IntegerCounter::merge);
    }

    public static <K> Collector<K, LongCounter<K>, LongCounter<K>> toLongCounter(){
        return Collector.of(LongCounter::new, LongCounter::increment, LongCounter::merge);
    }

    public static <K> Collector<K, DoubleCounter<K>, DoubleCounter<K>> toDoubleCounter(){
        return Collector.of(DoubleCounter::new, DoubleCounter::increment, DoubleCounter::merge);
    }

    public static <K> Collector<K, FloatCounter<K>, FloatCounter<K>> toFloatCounter(){
        return Collector.of(FloatCounter::new, FloatCounter::increment, FloatCounter::merge);
    }

}
