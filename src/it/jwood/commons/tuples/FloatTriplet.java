package it.jwood.commons.tuples;

public class FloatTriplet {

    public static FloatTriplet pivotFirst(FloatTriplet triplet){
        return new FloatTriplet(triplet.getFirst(), triplet.getThird(), triplet.getSecond());
    }

    public static FloatTriplet pivotThird(FloatTriplet triplet){
        return new FloatTriplet(triplet.getSecond(), triplet.getFirst(), triplet.getThird());
    }

    public static FloatTriplet pivotSecond(FloatTriplet triplet){
        return new FloatTriplet(triplet.getThird(), triplet.getSecond(), triplet.getFirst());
    }


    private float first;
    private float second;
    private float third;

    public FloatTriplet(float first, float second, float third){
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public float getFirst() {
        return first;
    }

    public float getSecond() {
        return second;
    }

    public float getThird() { return third; }

    public void setFirst(float first) {
        this.first = first;
    }

    public void setSecond(float second) {
        this.second = second;
    }

    public void setThird(float third) { this.third = third; }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = result * prime + Float.hashCode(first);
        result = result * prime + Float.hashCode(second);
        result = result * prime + Float.hashCode(third);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        FloatTriplet oth = (FloatTriplet) obj;
        if (first != oth.first)
            return false;
        if (second != oth.second)
            return false;
        if (third != oth.third)
            return false;
        return true;
    }
}
