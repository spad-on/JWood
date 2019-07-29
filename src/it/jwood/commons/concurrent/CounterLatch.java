package it.jwood.commons.concurrent;

import java.util.concurrent.atomic.AtomicLong;

public class CounterLatch {

    private final AtomicLong counter;

    public CounterLatch(){
        this.counter = new AtomicLong(0);
    }

    public void increment(){
        this.counter.incrementAndGet();
    }

    public void countDown(){
        long cnt = this.counter.decrementAndGet();
        if (cnt == 0){
            synchronized (counter){
                counter.notifyAll();
            }
        }
    }

    public void await() throws InterruptedException {
        synchronized (counter){
            while (counter.get() > 0){
                counter.wait();
            }
        }
    }
}
