package com.bmp.search.core.aggregator;

import com.bmp.commons.enums.DatasourceType;
import com.bmp.dao.entity.Datasource;
import com.bmp.search.core.Item;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class DatasourceTypeAggregator implements Aggregator {
    @Override
    public Aggregation aggregate(List<Item> items) {
        Counter<DatasourceType> datasourceType = new Counter<>();
        for (Item item : items) {
            Datasource datasource = item.getDatasource();
            if (datasource != null) {
                datasourceType.add(datasource.getType());
            }
        }
        return new Aggregation().setDatasourceType(datasourceType.getAll());
    }
}
