package it.jwood.commons.tuples;

public class DoubleLongPair {

    private double first;
    private long second;

    public DoubleLongPair(double first, long second){
        this.first = first;
        this.second = second;
    }

    public double getFirst() {
        return first;
    }

    public long getSecond() {
        return second;
    }

    public void setFirst(double first) {
        this.first = first;
    }

    public void setSecond(long second) {
        this.second = second;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = result * prime + Double.hashCode(first);
        result = result * prime + Long.hashCode(second);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        DoubleLongPair oth = (DoubleLongPair) obj;
        if (this.first != oth.first)
	    return false;
        if(this.second != oth.second)
            return false;
        return true;
    }

    public Pair<Double, Long> boxed(){
        return new Pair<>(first, second);
    }

    @Override
    public String toString() {
        return new StringBuilder().append("(").append(first).append(", ").append(second).append(")").toString();
    }
}