package edu.handong.csee.isel.fcminer.fpcollector.tokendiff;

public class Pair<T1, T2> {

    public final T1 first;

    public final T2 second;

    public Pair(T1 a, T2 b) {
        this.first = a;
        this.second = b;
    }

    public T1 getFirst() {
        return first;
    }

    public T2 getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair<?, ?> pair = (Pair<?, ?>) o;

        if (!first.equals(pair.first)) return false;
        return second.equals(pair.second);

    }

    @Override
    public int hashCode() {
        int result = first.hashCode();
        result = 31 * result + second.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "(" + getFirst().toString() + "," + getSecond().toString() + ")";
    }

}