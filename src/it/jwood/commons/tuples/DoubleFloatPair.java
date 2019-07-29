package it.jwood.commons.tuples;

public class DoubleFloatPair {

    private double first;
    private float second;

    public DoubleFloatPair(double first, float second){
        this.first = first;
        this.second = second;
    }

    public double getFirst() {
        return first;
    }

    public float getSecond() {
        return second;
    }

    public void setFirst(double first) {
        this.first = first;
    }

    public void setSecond(float second) {
        this.second = second;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = result * prime + Double.hashCode(first);
        result = result * prime + Float.hashCode(second);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        DoubleFloatPair oth = (DoubleFloatPair) obj;
        if (this.first != oth.first)
	    return false;
        if(this.second != oth.second)
            return false;
        return true;
    }

    public Pair<Double, Float> boxed(){
        return new Pair<>(first, second);
    }

    @Override
    public String toString() {
        return new StringBuilder().append("(").append(first).append(", ").append(second).append(")").toString();
    }
}