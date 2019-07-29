package it.jwood.commons.tuples;

public class LongTriplet {

    public static LongTriplet pivotFirst(LongTriplet triplet){
        return new LongTriplet(triplet.getFirst(), triplet.getThird(), triplet.getSecond());
    }

    public static LongTriplet pivotThird(LongTriplet triplet){
        return new LongTriplet(triplet.getSecond(), triplet.getFirst(), triplet.getThird());
    }

    public static LongTriplet pivotSecond(LongTriplet triplet){
        return new LongTriplet(triplet.getThird(), triplet.getSecond(), triplet.getFirst());
    }


    private long first;
    private long second;
    private long third;

    public LongTriplet(long first, long second, long third){
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public long getFirst() {
        return first;
    }

    public long getSecond() {
        return second;
    }

    public long getThird() { return third; }

    public void setFirst(long first) {
        this.first = first;
    }

    public void setSecond(long second) {
        this.second = second;
    }

    public void setThird(long third) { this.third = third; }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = result * prime + Long.hashCode(first);
        result = result * prime + Long.hashCode(second);
        result = result * prime + Long.hashCode(third);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        LongTriplet oth = (LongTriplet) obj;
        if (first != oth.first)
            return false;
        if (second != oth.second)
            return false;
        if (third != oth.third)
            return false;
        return true;
    }
}
