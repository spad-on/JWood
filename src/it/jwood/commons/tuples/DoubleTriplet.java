package it.jwood.commons.tuples;

public class DoubleTriplet {

    public static DoubleTriplet pivotFirst(DoubleTriplet triplet){
        return new DoubleTriplet(triplet.getFirst(), triplet.getThird(), triplet.getSecond());
    }

    public static DoubleTriplet pivotThird(DoubleTriplet triplet){
        return new DoubleTriplet(triplet.getSecond(), triplet.getFirst(), triplet.getThird());
    }

    public static DoubleTriplet pivotSecond(DoubleTriplet triplet){
        return new DoubleTriplet(triplet.getThird(), triplet.getSecond(), triplet.getFirst());
    }


    private double first;
    private double second;
    private double third;

    public DoubleTriplet(double first, double second, double third){
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public double getFirst() {
        return first;
    }

    public double getSecond() {
        return second;
    }

    public double getThird() { return third; }

    public void setFirst(double first) {
        this.first = first;
    }

    public void setSecond(double second) {
        this.second = second;
    }

    public void setThird(double third) { this.third = third; }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = result * prime + Double.hashCode(first);
        result = result * prime + Double.hashCode(second);
        result = result * prime + Double.hashCode(third);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        DoubleTriplet oth = (DoubleTriplet) obj;
        if (first != oth.first)
            return false;
        if (second != oth.second)
            return false;
        if (third != oth.third)
            return false;
        return true;
    }
}
