package com.bmp.connector.api;

public interface RowIterator extends AutoCloseable {
    boolean next() throws Exception;

    RowData getRow() throws Exception;
}
