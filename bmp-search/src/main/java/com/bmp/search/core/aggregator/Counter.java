package com.bmp.search.core.aggregator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Counter<T> {
    private final Map<T, MutableInteger> count = new HashMap<>();

    public void add(T t) {
        MutableInteger init = new MutableInteger();
        MutableInteger last = count.put(t, init);
        if (last != null) {
            init.val = last.val + 1;
        }
    }

    public void addAll(List<T> ts) {
        ts.forEach(this::add);
    }

    public int get(T t) {
        return count.get(t).val;
    }

    public Map<T, Integer> getAll() {
        Map<T, Integer> rst = new HashMap<>();
        count.forEach((key, value) -> rst.put(key, value.val));
        return rst;
    }


    public static class MutableInteger {
        private int val = 1;
    }
}
