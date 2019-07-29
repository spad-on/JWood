package it.jwood.commons.tuples;

public class Pair<S, T> {

    public static <T, S> Pair<S, T> invert(Pair<T, S> pair){
        return new Pair<>(pair.getSecond(), pair.getFirst());
    }

    private S first;
    private T second;

    public Pair(S first, T second){
        this.first = first;
        this.second = second;
    }

    public S getFirst() {
        return first;
    }

    public T getSecond() {
        return second;
    }

    public void setFirst(S first) {
        this.first = first;
    }

    public void setSecond(T second) {
        this.second = second;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = result * prime + (first == null ? 0 : first.hashCode());
        result = result * prime + (second == null ? 0 : second.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        Pair oth = (Pair) obj;
        if (this.first == null){
            if (oth.first != null)
                return false;
        }else if (!this.first.equals(oth.first))
            return false;
        if (this.second == null){
            if (oth.second != null)
                return false;
        }else if (!this.second.equals(oth.second))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("(").append(first).append(", ").append(second).append(")").toString();
    }
}
