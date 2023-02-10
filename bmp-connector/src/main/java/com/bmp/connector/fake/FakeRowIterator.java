package com.bmp.connector.fake;

import com.bmp.connector.api.RowData;
import com.bmp.connector.api.RowIterator;

public class FakeRowIterator implements RowIterator {
    public FakeRowIterator() {
    }

    @Override
    public boolean next() {
        return false;
    }

    @Override
    public RowData getRow() {
        return new RowData(0);
    }

    @Override
    public void close() {
    }
}
