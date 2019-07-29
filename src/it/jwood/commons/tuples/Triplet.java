package it.jwood.commons.tuples;

public class Triplet<S, T, U> {

    public static <T, S, U> Triplet<T, U, S> pivotFirst(Triplet<T, S, U> triplet){
        return new Triplet<>(triplet.getFirst(), triplet.getThird(), triplet.getSecond());
    }

    public static <T, S, U> Triplet<S, T, U> pivotThird(Triplet<T, S, U> triplet){
        return new Triplet<>(triplet.getSecond(), triplet.getFirst(), triplet.getThird());
    }

    public static <T, S, U> Triplet<U, S, T> pivotSecond(Triplet<T, S, U> triplet){
        return new Triplet<>(triplet.getThird(), triplet.getSecond(), triplet.getFirst());
    }


    private S first;
    private T second;
    private U third;

    public Triplet(S first, T second, U third){
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public S getFirst() {
        return first;
    }

    public T getSecond() {
        return second;
    }

    public U getThird() { return third; }

    public void setFirst(S first) {
        this.first = first;
    }

    public void setSecond(T second) {
        this.second = second;
    }

    public void setThird(U third) { this.third = third; }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = result * prime + (first == null ? 0 : first.hashCode());
        result = result * prime + (second == null ? 0 : second.hashCode());
        result = result * prime + (third == null ? 0 : third.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        Triplet oth = (Triplet) obj;
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
        if (this.third == null){
            if (oth.third != null)
                return false;
        }else if (!this.third.equals(oth.third))
            return false;
        return true;
    }
}
