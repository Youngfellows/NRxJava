package com.necho.nrxjava.generics;

/**
 * 包含三个泛型的泛型类
 */
public class ThreeTuple<A, B, C> {
    public A first;
    public B second;
    public C three;

    public ThreeTuple(A first, B second, C three) {
        this.first = first;
        this.second = second;
        this.three = three;
    }

    @Override
    public String toString() {
        return "ThreeTuple{" +
                "first=" + first +
                ", second=" + second +
                ", three=" + three +
                '}';
    }
}
