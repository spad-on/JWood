package it.jwood.commons.tuples;

public class IntPair {

    public static  IntPair invert(IntPair pair){
        return new IntPair(pair.getSecond(), pair.getFirst());
    }

    private int first;
    private int second;

    public IntPair(int first, int second){
        this.first = first;
        this.second = second;
    }

    public int getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = result * prime + first;
        result = result * prime + second;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        IntPair oth = (IntPair) obj;
        if (this.first != oth.first)
            return false;
        if (this.second != oth.second)
            return false;
        return true;
    }

    public Pair<Integer, Integer> boxed(){
        return new Pair<>(first, second);
    }

    @Override
    public String toString() {
        return new StringBuilder().append("(").append(first).append(", ").append(second).append(")").toString();
    }
}
