package com.bmp.search.core.aggregator;

import com.bmp.commons.enums.SubjectType;
import com.bmp.search.core.Item;

import java.util.List;

public class SubjectTypeAggregator implements Aggregator {
    @Override
    public Aggregation aggregate(List<Item> items) {
        Counter<SubjectType> subjectType = new Counter<>();
        for (Item item : items) {
            subjectType.add(item.getType());
        }
        return new Aggregation().setSubjectType(subjectType.getAll());
    }
}
