package org.example.connector.api.alignment;

public interface CompareFunc<A extends IColumn, B extends IColumn> {
    boolean apply(int ai, int bi, A a, B b);
}
