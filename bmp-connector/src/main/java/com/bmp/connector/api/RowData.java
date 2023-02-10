package com.bmp.connector.api;

public class RowData {
    private final Object[] values;

    public RowData(int arity) {
        this.values = new Object[arity];
    }

    public Object getValue(int i) {
        return values[i];
    }

    public Object[] getValues() {
        return values;
    }

    public void setValue(int i, Object value) {
        values[i] = value;
    }
}
