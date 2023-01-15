package com.github.bmp.search.core.aggregator;

import com.github.bmp.commons.enums.DatasourceType;
import com.github.bmp.commons.enums.SubjectType;
import com.github.bmp.dao.entity.Collection;
import com.github.bmp.dao.entity.Datasource;
import com.github.bmp.dao.entity.Tag;

import java.util.Map;

/**
 * We aggregate all search items base on different type.
 */
public class Aggregation {
    /**
     * Aggregate by {@link DatasourceType}.
     */
    private Map<DatasourceType, Integer> datasourceType;
    /**
     * Aggregate by {@link SubjectType}
     */
    private Map<SubjectType, Integer> subjectType;
    /**
     * Aggregate by {@link Collection#getId()}.
     */
    private Map<Integer, Integer> collection;
    /**
     * Aggregate by {@link Datasource#getId()}.
     */
    private Map<Integer, Integer> datasource;

    /**
     * Aggregate by {@link Tag#getId()}.
     */
    private Map<Integer, Integer> tag;

    public void union(Aggregation other) {
        // TODO: union
    }
}
