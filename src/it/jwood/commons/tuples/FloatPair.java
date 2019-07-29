package it.jwood.commons.tuples;

public class FloatPair {

    public static FloatPair invert(FloatPair pair){
        return new FloatPair(pair.getSecond(), pair.getFirst());
    }

    private float first;
    private float second;

    public FloatPair(float first, float second){
        this.first = first;
        this.second = second;
    }

    public float getFirst() {
        return first;
    }

    public float getSecond() {
        return second;
    }

    public void setFirst(float first) {
        this.first = first;
    }

    public void setSecond(float second) {
        this.second = second;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = result * prime + Float.hashCode(first);
        result = result * prime + Float.hashCode(second);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        FloatPair oth = (FloatPair) obj;
        if (this.first != oth.first)
            return false;
        if (this.second != oth.second)
            return false;
        return true;
    }

    public Pair<Float, Float> boxed(){
        return new Pair<>(first, second);
    }

    @Override
    public String toString() {
        return new StringBuilder().append("(").append(first).append(", ").append(second).append(")").toString();
    }
}
