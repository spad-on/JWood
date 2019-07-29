package it.jwood.commons.time;

import java.util.HashMap;
import java.util.Map;

public abstract class Bar {

    protected static final String BOL = "\r"; //"\33[1A\33[2K";
    protected static String TICK = "=";
    protected static int MAX_LENGTH = 70;

    public static void setMaxLength(int maxLength) {
        MAX_LENGTH = maxLength;
    }

    public static void setTICK(String TICK) {
        TICK = TICK;
    }


    protected int current = 0;
    private boolean closed;
    protected Map<String, Object> postfixes = new HashMap<>();



    public Bar(){
        System.err.println();
    }

    public void update(int add){
        this.current += add;
        display();
    }

    public void update(){
        this.current++;
        display();
    }

    public void addPostfix(String key, Object value){
        this.postfixes.put(key, value);
    }

    public void removePostfix(String key){
        this.postfixes.remove(key);
    }

    protected abstract void display();

    public void close(){
        if (!closed) {
            System.err.println();
            closed = true;
        }
    }
}
