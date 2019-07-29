package it.jwood.commons.concurrent;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Pool implements ExecutorService {

    private static final int DEFAULT_LOGGING_STEP = 10000;
    private static Pool instance = null;

    public static Pool getCommonPool() {
        if (instance == null){
            synchronized (Pool.class){
                if (instance == null){
                    instance = new Pool(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
                }
            }
        }
        return instance;
    }

    public static Pool newInstance(ExecutorService service){
        Objects.requireNonNull(service);
        return new Pool(service);
    }

    private ExecutorService pool;
    private Logger logger;
    private int loggingStep;

    private Pool(ExecutorService service){
        this.loggingStep = DEFAULT_LOGGING_STEP;
        this.pool = service;
        // set up logger
        this.logger = Logger.getLogger("Pool");
        this.logger.setLevel(Level.INFO);
    }

    public void setLoggingStep(int loggingStep) {
        this.loggingStep = loggingStep;
    }

    public void setLoggingLevel(Level level){
        this.logger.setLevel(level);
    }

    public Level getLoggingLevel(){
        return this.logger.getLevel();
    }


    @Override
    public void shutdown() {
        pool.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return pool.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return pool.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return pool.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return pool.awaitTermination(timeout, unit);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return pool.submit(task);
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return pool.submit(task, result);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return pool.submit(task);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return pool.invokeAll(tasks);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return pool.invokeAll(tasks, timeout, unit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return pool.invokeAny(tasks);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return pool.invokeAny(tasks, timeout, unit);
    }

    public <T> void execute(Collection<T> collection, final BiConsumer<Integer, ? super T> command){
        Objects.requireNonNull(command);
        Objects.requireNonNull(collection);
        final int total = collection.size();
        final CountDownLatch latch = new CountDownLatch(total);
        int index = 0;
        for (T el : collection){
            int idx = index++;
            pool.execute(() -> {
                try {
                    int ii = idx;
                    command.accept(ii, el);
                    if (ii % loggingStep == 0) {
                        logger.log(Level.INFO, String.format("Done: %.2f%%", 100.0 * ii / total));
                    }
                }finally {
                    latch.countDown();
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) { }
    }


    public <T> void execute(Collection<T> collection, final Consumer<? super T> command){
        Objects.requireNonNull(command);
        Objects.requireNonNull(collection);
        final int total = collection.size();
        final CountDownLatch latch = new CountDownLatch(total);
        int index = 0;
        for (T el : collection){
            int idx = index++;
            pool.execute(() -> {
                try {
                    int ii = idx;
                    command.accept(el);
                    if ( ii % loggingStep == 0){
                        logger.log(Level.INFO, String.format("Done: %.2f%%", 100.0*ii/total));
                    }
                }finally {
                    latch.countDown();
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) { }
    }


    public <T> void execute(Iterator<T> it, final Consumer< ? super T> command){
        Objects.requireNonNull(command);
        Objects.requireNonNull(it);
        final CounterLatch latch = new CounterLatch();
        long index = 0;
        while(it.hasNext()) {
            T el = it.next();
            latch.increment();
            long idx = index;
            index++;
            pool.execute(() -> {
                try{
                    long ii = idx;
                    command.accept(el);
                    if ( ii % loggingStep == 0){
                        logger.log(Level.INFO, "Done: "+ ii);
                    }
                }finally {
                    latch.countDown();
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {}
    }

    public <T> void execute(Iterator<T> it, final BiConsumer<Long, ? super T> command){
        Objects.requireNonNull(command);
        Objects.requireNonNull(it);
        final CounterLatch latch = new CounterLatch();
        long index = 0;
        while(it.hasNext()) {
            T el = it.next();
            latch.increment();
            long idx = index;
            index++;
            pool.execute(() -> {
                try{
                    long ii = idx;
                    command.accept(ii, el);
                    if ( ii % loggingStep == 0){
                        logger.log(Level.INFO, "Done: "+ ii);
                    }
                }finally {
                    latch.countDown();
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {}
    }


    public <T> void execute(Stream<T> stream, final BiConsumer<Long, ? super T> command){
        Objects.requireNonNull(command);
        Objects.requireNonNull(stream);
        final CounterLatch latch = new CounterLatch();
        AtomicBoolean closed = new AtomicBoolean(false);
        AtomicLong index = new AtomicLong();
        stream.sequential().onClose(() -> {
            synchronized (closed){
                closed.set(true);
                closed.notifyAll();
            }
        }).forEach(el -> {
            latch.increment();
            long ii = index.getAndIncrement();
            pool.execute(() -> {
                try{
                    command.accept(ii, el);
                    if ( ii % loggingStep == 0){
                        logger.log(Level.INFO, "Done: "+ ii);
                    }
                }finally {
                    latch.countDown();
                }
            });
        });
        try {
            synchronized (closed) {
                while (!closed.get()){
                    closed.wait();
                }
            }
            latch.await();
        } catch (InterruptedException e) { }
    }

    public <T> void execute(Stream<T> stream, final Consumer<? super T> command){
        Objects.requireNonNull(command);
        Objects.requireNonNull(stream);
        final CounterLatch latch = new CounterLatch();
        AtomicBoolean closed = new AtomicBoolean(false);
        AtomicLong index = new AtomicLong();
        stream.sequential().onClose(() -> {
            synchronized (closed){
                closed.set(true);
                closed.notifyAll();
            }
        }).forEach(el -> {
            latch.increment();
            long ii = index.getAndIncrement();
            pool.execute(() -> {
                try{
                    command.accept(el);
                    if ( ii % loggingStep == 0){
                        logger.log(Level.INFO, "Done: "+ ii);
                    }
                }finally {
                    latch.countDown();
                }
            });
        });
        try {
            synchronized (closed) {
                while (!closed.get()){
                    closed.wait();
                }
            }
            latch.await();
        } catch (InterruptedException e) { }
    }

    public void execute(IntStream stream, final IntConsumer command){
        Objects.requireNonNull(command);
        Objects.requireNonNull(stream);
        final CounterLatch latch = new CounterLatch();
        AtomicBoolean closed = new AtomicBoolean(false);
        AtomicLong index = new AtomicLong();
        stream.sequential().onClose(() -> {
            synchronized (closed){
                closed.set(true);
                closed.notifyAll();
            }
        }).forEach(el -> {
            latch.increment();
            long ii = index.getAndIncrement();
            pool.execute(() -> {
                try{
                    command.accept(el);
                    if ( ii % loggingStep == 0){
                        logger.log(Level.INFO, "Done: "+ ii);
                    }
                }finally {
                    latch.countDown();
                }
            });
        });
        try {
            synchronized (closed) {
                while (!closed.get()){
                    closed.wait();
                }
            }
            latch.await();
        } catch (InterruptedException e) { }
    }

    public void execute(DoubleStream stream, final DoubleConsumer command){
        Objects.requireNonNull(command);
        Objects.requireNonNull(stream);
        final CounterLatch latch = new CounterLatch();
        AtomicBoolean closed = new AtomicBoolean(false);
        AtomicLong index = new AtomicLong();
        stream.sequential().onClose(() -> {
            synchronized (closed){
                closed.set(true);
                closed.notifyAll();
            }
        }).forEach(el -> {
            latch.increment();
            long ii = index.getAndIncrement();
            pool.execute(() -> {
                try{
                    command.accept(el);
                    if ( ii % loggingStep == 0){
                        logger.log(Level.INFO, "Done: "+ ii);
                    }
                }finally {
                    latch.countDown();
                }
            });
        });
        try {
            synchronized (closed) {
                while (!closed.get()){
                    closed.wait();
                }
            }
            latch.await();
        } catch (InterruptedException e) { }
    }

    public void execute(LongStream stream, final LongConsumer command){
        Objects.requireNonNull(command);
        Objects.requireNonNull(stream);
        final CounterLatch latch = new CounterLatch();
        AtomicBoolean closed = new AtomicBoolean(false);
        AtomicLong index = new AtomicLong();
        stream.sequential().onClose(() -> {
            synchronized (closed){
                closed.set(true);
                closed.notifyAll();
            }
        }).forEach(el -> {
            latch.increment();
            long ii = index.getAndIncrement();
            pool.execute(() -> {
                try{
                    command.accept(el);
                    if ( ii % loggingStep == 0){
                        logger.log(Level.INFO, "Done: "+ ii);
                    }
                }finally {
                    latch.countDown();
                }
            });
        });
        try {
            synchronized (closed) {
                while (!closed.get()){
                    closed.wait();
                }
            }
            latch.await();
        } catch (InterruptedException e) { }
    }

    public <T> void execute(int[] elements, final IntConsumer command){
        Objects.requireNonNull(command);
        Objects.requireNonNull(elements);
        final int total = elements.length;
        final CountDownLatch latch = new CountDownLatch(total);
        int index = 0;
        for (int el : elements){
            int idx = index++;
            pool.execute(() -> {
                try{
                    int ii = idx;
                    command.accept(el);
                    if ( ii % loggingStep == 0){
                        logger.log(Level.INFO, String.format("Done: %.2f%%", 100.0*ii/total));
                    }
                }finally {
                    latch.countDown();
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) { }
    }

    public <T> void execute(double[] elements, final DoubleConsumer command){
        Objects.requireNonNull(command);
        Objects.requireNonNull(elements);
        final int total = elements.length;
        final CountDownLatch latch = new CountDownLatch(total);
        int index =0;
        for (double el : elements){
            int idx = index++;
            pool.execute(() -> {
                try{
                    int ii = idx;
                    command.accept(el);
                    if ( ii % loggingStep == 0){
                        logger.log(Level.INFO, String.format("Done: %.2f%%", 100.0*ii/total));
                    }
                }finally {
                    latch.countDown();
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) { }
    }

    public <T> void execute(long[] elements, final LongConsumer command){
        Objects.requireNonNull(command);
        Objects.requireNonNull(elements);
        final int total = elements.length;
        final CountDownLatch latch = new CountDownLatch(total);
        int index = 0;
        for (long el : elements){
            int idx = index++;
            pool.execute(() -> {
                try{
                    int ii = idx;
                    command.accept(el);
                    if ( ii % loggingStep == 0){
                        logger.log(Level.INFO, String.format("Done: %.2f%%", 100.0*ii/total));
                    }
                }finally {
                    latch.countDown();
                }
            });
        }
        try {
            System.err.println("WAITING");
            latch.await();
            System.err.println("DONE WAITING");
        } catch (InterruptedException e) { }
    }

    public <T> void execute(final LongConsumer command, long ... elements) {
        execute(elements, command);
    }

    public <T> void execute(final IntConsumer command, int ... elements) {
        execute(elements, command);
    }

    public <T> void execute(final DoubleConsumer command, double ... elements) {
        execute(elements, command);
    }


    @Override
    public void execute(Runnable command) {
        pool.execute(command);
    }
}
