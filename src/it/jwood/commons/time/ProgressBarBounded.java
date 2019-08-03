package it.jwood.commons.time;

import java.util.Iterator;
import java.util.function.Consumer;

class ProgressBarBounded extends ProgressBar {

    private static final String BOL = "\r";
    private static String TICK = "=";
    private static int MAX_LENGTH = 70;

    public static void setMaxLength(int maxLength) {
        MAX_LENGTH = maxLength;
    }

    public static void setTICK(String TICK) {
        ProgressBarBounded.TICK = TICK;
    }

    private int total;
    private long avgDiffTime;
    private long lastTime;

    protected ProgressBarBounded(int total){
        super();
        this.total = total;
        this.lastTime = -1;
        display();
    }


    public void addPostfix(String key, Object value){
        this.postfixes.put(key, value);
    }

    public void removePostfix(String key){
        this.postfixes.remove(key);
    }

    public int getTotal() {
        return total;
    }

    protected void display(){
        StringBuilder postfix = new StringBuilder();
        postfixes.forEach((k, v) -> postfix.append(k).append(": ").append(v).append(" "));

        String eta = "";
        long currentTime = System.currentTimeMillis();
        if (this.lastTime > 0){
            long diff = currentTime - lastTime;
            avgDiffTime += diff;
            double second = (1.*avgDiffTime/(current*1000) * (total - current));
            long min = (long)second/60;
            min %= 60;
            long hours = (long)second/3600;
            double sec = second - hours*3600 - min*60;
            eta = String.format("%dh %dm %.2fs", (int)hours, (int)min, sec);
        }
        lastTime = currentTime;
        float percentage = 100.f*current/total;
        StringBuilder progress = new StringBuilder();
        int thicks = total <=0 ? 0 : MAX_LENGTH*current/total;
        for (int i = 0; i < thicks; i++) {
            if (i == thicks-1 && i != MAX_LENGTH-1)
                progress.append(">");
            else
                progress.append(TICK);
        }
        for (int i = thicks; i < MAX_LENGTH; i++)
            progress.append(" ");
        System.err.print(BOL);
        System.err.printf("ETA: '%s' %.2f%%[%s] %s", eta, percentage, progress.toString(), postfix.toString().trim());
    }


    static class LogIterator<T> implements Iterator<T> {

        private final Iterator<T> it;
        private final ProgressBarBounded bar;

        public LogIterator(Iterator<T> it, int total){
            this.it = it;
            this.bar = new ProgressBarBounded(total);
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
