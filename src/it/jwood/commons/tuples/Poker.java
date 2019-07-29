package it.jwood.commons.tuples;

public class Poker<S, T, U, V> {

    private S first;
    private T second;
    private U third;
    private V fourth;

    public Poker(S first, T second, U third, V fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }

    public S getFirst() {
        return first;
    }

    public T getSecond() {
        return second;
    }

    public U getThird() { return third; }

    public V getFourth() { return fourth; }

    public void setFirst(S first) {
        this.first = first;
    }

    public void setSecond(T second) {
        this.second = second;
    }

    public void setThird(U third) { this.third = third; }

    public void setFourth(V fourth) { this.fourth = fourth; }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = result * prime + (first == null ? 0 : first.hashCode());
        result = result * prime + (second == null ? 0 : second.hashCode());
        result = result * prime + (third == null ? 0 : third.hashCode());
        result = result * prime + (fourth == null ? 0 : fourth.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        Poker oth = (Poker) obj;
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
        if (this.fourth == null){
            if (oth.fourth != null)
                return false;
        }else if (!this.fourth.equals(oth.fourth))
            return false;
        return true;
    }
}
