package it.jwood.commons.tuples;

public class IntTriplet {

    public static IntTriplet pivotFirst(IntTriplet triplet){
        return new IntTriplet(triplet.getFirst(), triplet.getThird(), triplet.getSecond());
    }

    public static  IntTriplet pivotThird(IntTriplet triplet){
        return new IntTriplet(triplet.getSecond(), triplet.getFirst(), triplet.getThird());
    }

    public static IntTriplet pivotSecond(IntTriplet triplet){
        return new IntTriplet(triplet.getThird(), triplet.getSecond(), triplet.getFirst());
    }


    private int first;
    private int second;
    private int third;

    public IntTriplet(int first, int second, int third){
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public int getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }

    public int getThird() { return third; }

    public void setFirst(int first) {
        this.first = first;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public void setThird(int third) { this.third = third; }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = result * prime + first;
        result = result * prime + second;
        result = result * prime + third;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        IntTriplet oth = (IntTriplet) obj;
        if (first != oth.first)
            return false;
        if (second != oth.second)
            return false;
        if (third != oth.third)
            return false;
        return true;
    }
}
