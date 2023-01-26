package com.bmp.connector.api.alignment;

public interface CompareFunc<A extends IColumn, B extends IColumn> {
    /**
     * compare columns
     *
     * @param ai index of columns A
     * @param bi index of columns B
     * @param a  column A
     * @param b  column B
     * @return elements match
     */
    boolean apply(int ai, int bi, A a, B b);
}
