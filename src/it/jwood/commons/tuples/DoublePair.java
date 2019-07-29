package it.jwood.commons.tuples;

public class DoublePair {

    public static DoublePair invert(DoublePair pair){
        return new DoublePair(pair.getSecond(), pair.getFirst());
    }

    private double first;
    private double second;

    public DoublePair(double first, double second){
        this.first = first;
        this.second = second;
    }

    public double getFirst() {
        return first;
    }

    public double getSecond() {
        return second;
    }

    public void setFirst(double first) {
        this.first = first;
    }

    public void setSecond(double second) {
        this.second = second;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = result * prime + Double.hashCode(first);
        result = result * prime + Double.hashCode(second);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        DoublePair oth = (DoublePair) obj;
        if (this.first != oth.first)
            return false;
        if (this.second != oth.second)
            return false;
        return true;
    }

    public Pair<Double, Double> boxed(){
        return new Pair<>(first, second);
    }

    @Override
    public String toString() {
        return new StringBuilder().append("(").append(first).append(", ").append(second).append(")").toString();
    }
}
