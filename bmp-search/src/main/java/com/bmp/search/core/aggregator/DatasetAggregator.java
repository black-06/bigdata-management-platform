package com.bmp.search.core.aggregator;

import com.bmp.dao.entity.Subject;
import com.bmp.search.core.Item;

import java.util.List;

public class DatasetAggregator implements Aggregator {
    @Override
    public Aggregation aggregate(List<Item> items) {
        Counter<Integer> collectionIDs = new Counter<>();
        Counter<Integer> datasourceIDs = new Counter<>();
        for (Item item : items) {
            Subject collection = item.getCollection();
            if (collection != null) {
                collectionIDs.add(collection.getId());
            }
            Subject datasource = item.getDatasource();
            if (datasource != null) {
                datasourceIDs.add(datasource.getId());
            }
        }
        return new Aggregation()
                .setCollection(collectionIDs.getAll())
                .setDatasource(datasourceIDs.getAll());
    }
}
