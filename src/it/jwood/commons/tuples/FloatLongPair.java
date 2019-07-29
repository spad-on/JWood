package it.jwood.commons.tuples;

public class FloatLongPair {

    private float first;
    private long second;

    public FloatLongPair(float first, long second){
        this.first = first;
        this.second = second;
    }

    public float getFirst() {
        return first;
    }

    public long getSecond() {
        return second;
    }

    public void setFirst(float first) {
        this.first = first;
    }

    public void setSecond(long second) {
        this.second = second;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = result * prime + Float.hashCode(first);
        result = result * prime + Long.hashCode(second);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        FloatLongPair oth = (FloatLongPair) obj;
        if (this.first != oth.first)
	    return false;
        if(this.second != oth.second)
            return false;
        return true;
    }

    public Pair<Float, Long> boxed(){
        return new Pair<>(first, second);
    }

    @Override
    public String toString() {
        return new StringBuilder().append("(").append(first).append(", ").append(second).append(")").toString();
    }
}