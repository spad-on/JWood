package it.jwood.commons.time;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import java.util.function.LongSupplier;

public class Timer {


    public static String formatMillis(String pattern, long time){
        ZonedDateTime zdt = ZonedDateTime.ofInstant ( Instant.ofEpochMilli(time), ZoneOffset.UTC );
        String format = DateTimeFormatter.ofPattern(pattern).format(zdt);
        return format;
    }

    public static String formatNaonsec(String pattern, long time){
        return formatMillis(pattern, (long)(time*1e-6));
    }

    public static long wrap(Runnable runnable){
        return wrap(runnable, false);
    }

    public static long wrap(Runnable runnable, boolean nanosec){
        Timer timer = new Timer(nanosec);
        timer.start();
        runnable.run();
        return timer.stop();
    }

    private long startTime;
    private long lastTime;
    private long lapTime;
    private LongSupplier time;
    private boolean isRunning;
    private boolean hasStarted;
    private TimeUnit timeUnit;

    public Timer(boolean nanosec){
        this.time = nanosec ? System::nanoTime : System::currentTimeMillis;
        this.isRunning = false;
        this.timeUnit = nanosec ? TimeUnit.NANOSECONDS : TimeUnit.MILLISECONDS;
    }

    public Timer(){
        this(false);
    }

    public long start(){
        this.startTime = time.getAsLong();
        this.lastTime = startTime;
        this.lapTime = startTime;
        this.isRunning = true;
        this.hasStarted = true;
        return startTime;
    }

    public long stop(){
        this.isRunning = false;
        this.hasStarted = false;
        lastTime = time.getAsLong();
        return lastTime - startTime;
    }

    public long pause(){
        if (!hasStarted)
            throw new IllegalStateException("Call start() before pause.");
        isRunning = false;
        lastTime = time.getAsLong();
        return lastTime - startTime;
    }

    public void resume(){
        if (!hasStarted)
            throw new IllegalStateException("Call start() before resume.");
        long t = time.getAsLong();
        long elapsedTime = lastTime - startTime;
        this.startTime = t - elapsedTime;
        this.lapTime = t - (lastTime - lapTime);
        this.lastTime = t;
        isRunning = true;
    }


    public long getElapsedTime(){
        return isRunning ? time.getAsLong() - startTime : lastTime - startTime;
    }

    public long getStartingTime(){
        return startTime;
    }

    public long getLastTime(){
        return lastTime;
    }

    public long lap(){
        if (!isRunning)
            throw new IllegalStateException("Timer is not running. Make sure start() is called.");
        long t = lapTime;
        lapTime = time.getAsLong();
        return lapTime - t;
    }

    public TimeUnit getTimeUnit(){
        return timeUnit;
    }




}
