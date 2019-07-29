package it.jwood.commons.time;

import java.util.Iterator;
import java.util.function.Consumer;

public class ProgressBarUnbounded extends Bar {


    private static final String BOL = "\r"; //"\33[1A\33[2K";
    private static String TICK = "=";
    private static int TICK_PROGRESSION = 100000;


    public static <T> Iterable<T> log(Iterator<T> iterator){
        return log(iterator, TICK_PROGRESSION);
    }

    public static <T> Iterable<T> log(Iterator<T> iterator, int progressionStep){
        return () -> new LogIteratorUnbounded<>(iterator, progressionStep);
    }

    private int progressionStep = TICK_PROGRESSION;
    private long lastTime;
    private long avgDiffTime;

    public ProgressBarUnbounded(int progressionStep){
        super();
        this.progressionStep = progressionStep;
        this.lastTime = -1;
        display();

    }

    public ProgressBarUnbounded(){
        this(TICK_PROGRESSION);
    }

    public int getProgressionStep() {
        return progressionStep;
    }


    protected void display(){
        StringBuilder postfix = new StringBuilder();
        postfixes.forEach((k, v) -> postfix.append(k).append(": ").append(v).append(" "));

        String its = "";
        long currentTime = System.currentTimeMillis();
        if (this.lastTime > 0){
            long diff = currentTime - lastTime;
            avgDiffTime += diff;
            double second = 1./ (1.*avgDiffTime/(current*1000));
            its = String.format("%.2f it/s", second);
        }
        lastTime = currentTime;
        StringBuilder progress = new StringBuilder();
        int thicks = progressionStep <= 0 ? 0 : current/progressionStep;
        for (int i = 0; i < thicks; i++) {
            progress.append(TICK);
        }
        progress.append(">");
        System.err.print(BOL);
        System.err.printf("%s %d [%s] %s", its, current, progress.toString(), postfix.toString().trim());
    }


    private static class LogIteratorUnbounded<T> implements Iterator<T> {

        private final Iterator<T> it;
        private final ProgressBarUnbounded bar;

        public LogIteratorUnbounded(Iterator<T> it){
            this(it, TICK_PROGRESSION);
        }

        public LogIteratorUnbounded(Iterator<T> it, int progressionStep){
            this.bar = new ProgressBarUnbounded(progressionStep);
            this.it = it;
        }

        @Override
        public boolean hasNext() {
            boolean hasNext = it.hasNext();
            if (!hasNext)
                bar.close();
            return hasNext;
        }

        @Override
        public T next() {
            bar.update();
            return it.next();
        }

        @Override
        public void remove() {
            it.remove();
        }

        @Override
        public void forEachRemaining(Consumer<? super T> consumer) {
            it.forEachRemaining(s -> {bar.update(); consumer.accept(s);});
            bar.close();
        }
    }
}
