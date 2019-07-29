package it.jwood.commons.tuples;

public class LongPair {

    public static LongPair invert(LongPair pair){
        return new LongPair(pair.getSecond(), pair.getFirst());
    }

    private long first;
    private long second;

    public LongPair(long first, long second){
        this.first = first;
        this.second = second;
    }

    public long getFirst() {
        return first;
    }

    public long getSecond() {
        return second;
    }

    public void setFirst(long first) {
        this.first = first;
    }

    public void setSecond(long second) {
        this.second = second;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = result * prime + Long.hashCode(first);
        result = result * prime + Long.hashCode(second);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        LongPair oth = (LongPair) obj;
        if (this.first != oth.first)
            return false;
        if (this.second != oth.second)
            return false;
        return true;
    }

    public Pair<Long, Long> boxed(){
        return new Pair<>(first, second);
    }

    @Override
    public String toString() {
        return new StringBuilder().append("(").append(first).append(", ").append(second).append(")").toString();
    }
}
