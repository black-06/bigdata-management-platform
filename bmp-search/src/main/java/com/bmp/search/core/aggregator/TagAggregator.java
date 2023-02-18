package com.bmp.search.core.aggregator;

import com.bmp.search.core.Item;

import java.util.List;

public class TagAggregator implements Aggregator {

    @Override
    public Aggregation aggregate(List<Item> items) {
        Counter<Integer> counter = new Counter<>();
        for (Item item : items) {
            item.getTags().forEach(tag -> counter.add(tag.getId()));
        }
        return new Aggregation().setTag(counter.getAll());
    }
}
