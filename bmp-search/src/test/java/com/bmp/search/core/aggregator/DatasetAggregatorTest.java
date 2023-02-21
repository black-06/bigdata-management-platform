package com.bmp.search.core.aggregator;

import com.bmp.dao.entity.Collection;
import com.bmp.dao.entity.Datasource;
import com.bmp.search.core.Item;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.bmp.search.core.aggregator.AggregationTest.assertAggregationEquals;

class DatasetAggregatorTest {

    Item mockItem(Integer collectionID, Integer datasourceID) {
        Item item = new Item();
        if (collectionID != null) {
            item.setCollection(new Collection().setId(collectionID));
        }
        if (datasourceID != null) {
            item.setDatasource(new Datasource().setId(datasourceID));
        }
        return item;
    }

    @Test
    void aggregate() {
        DatasetAggregator aggregator = new DatasetAggregator();
        Aggregation aggregation = aggregator.aggregate(Arrays.asList(
                mockItem(1, null),
                mockItem(1, 1),
                mockItem(2, 2),
                mockItem(null, 3),
                mockItem(null, null)
        ));

        Map<Integer, Integer> collection = new HashMap<>();
        collection.put(1, 2);
        collection.put(2, 1);

        Map<Integer, Integer> datasource = new HashMap<>();
        datasource.put(1, 1);
        datasource.put(2, 1);
        datasource.put(3, 1);

        Aggregation expected = new Aggregation()
                .setCollection(collection)
                .setDatasource(datasource);
        assertAggregationEquals(expected, aggregation);
    }
}