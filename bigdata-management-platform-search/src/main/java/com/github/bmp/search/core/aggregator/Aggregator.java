package com.github.bmp.search.core.aggregator;

import com.github.bmp.search.core.Item;

import java.util.List;


public interface Aggregator {
    Aggregation aggregate(List<Item> items);
}
