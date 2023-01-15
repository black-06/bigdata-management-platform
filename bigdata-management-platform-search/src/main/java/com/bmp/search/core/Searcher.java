package com.bmp.search.core;

import com.bmp.search.core.aggregator.Aggregation;
import com.bmp.search.core.aggregator.Aggregator;
import com.bmp.search.core.filter.BatchFilter;
import com.bmp.search.core.packer.Packer;
import com.bmp.search.core.provider.Provider;
import com.bmp.search.core.ranker.Ranker;

import java.util.List;

public class Searcher {
    private final SearchRequest request;

    private Provider provider;
    private List<BatchFilter> filters;
    private Ranker ranker;
    private List<Aggregator> aggregators;
    private List<Packer> packers;

    private Searcher(SearchRequest request) {
        this.request = request;
        // TODO: init searcher
    }

    public SearchResponse searchInternal() throws RuntimeException {
        List<Item> items = provider.recall(request);

        for (BatchFilter filter : filters) {
            items = filter.filter(items);
        }

        items = ranker.rank(items);

        Aggregation aggregation = new Aggregation();
        for (Aggregator aggregator : aggregators) {
            aggregation.union(aggregator.aggregate(items));
        }

        return new SearchResponse(items, aggregation, items.size());
    }

    public static SearchResponse search(SearchRequest request) {
        // TODO: cache
        return new Searcher(request).searchInternal();
    }
}
