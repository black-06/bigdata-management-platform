package com.bmp.connector.api;

public abstract class LimitPushdown {
    protected int limit;

    public void applyLimit(int limit) {
        this.limit = limit;
    }
}
