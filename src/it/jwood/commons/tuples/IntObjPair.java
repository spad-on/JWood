package it.jwood.commons.tuples;

public class IntObjPair<T> {

    private int first;
    private T second;

    public IntObjPair(int first, T second){
        this.first = first;
        this.second = second;
    }

    public int getFirst() {
        return first;
    }

    public T getSecond() {
        return second;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public void setSecond(T second) {
        this.second = second;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = result * prime + first;
        result = result * prime + second.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        IntObjPair oth = (IntObjPair) obj;
        if (this.first != oth.first)
	    return false;
        if(this.second == null){
			if (oth.second != null)
				return false;
		}else if (!this.second.equals(oth.second))
            return false;
        return true;
    }

    public Pair<Integer, T> boxed(){
        return new Pair<>(first, second);
    }

    @Override
    public String toString() {
        return new StringBuilder().append("(").append(first).append(", ").append(second).append(")").toString();
    }
}