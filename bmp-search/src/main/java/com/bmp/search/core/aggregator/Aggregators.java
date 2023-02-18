package com.bmp.search.core.aggregator;

import com.bmp.search.core.Item;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class Aggregators implements Aggregator {
    public static final Aggregators Instance = new Aggregators(Arrays.asList(
            new DatasetAggregator(),
            new DatasourceTypeAggregator(),
            new SubjectTypeAggregator(),
            new TagAggregator()
    ));

    private final List<Aggregator> aggregators;

    @Override
    public Aggregation aggregate(List<Item> items) {
        Aggregation aggregation = new Aggregation();
        for (Aggregator aggregator : aggregators) {
            aggregation.union(aggregator.aggregate(items));
        }
        return aggregation;
    }
}
