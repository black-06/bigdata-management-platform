package com.bmp.search.core.aggregator;

import com.bmp.commons.enums.DatasourceType;
import com.bmp.commons.enums.SubjectType;
import com.bmp.dao.entity.Collection;
import com.bmp.dao.entity.Datasource;
import com.bmp.dao.entity.Tag;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * We aggregate all search items base on different type.
 */
@Data
@Accessors(chain = true)
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
        this.datasourceType = merge(this.datasourceType, other.datasourceType);
        this.subjectType = merge(this.subjectType, other.subjectType);
        this.collection = merge(this.collection, other.collection);
        this.datasource = merge(this.datasource, other.datasource);
        this.tag = merge(this.tag, other.tag);
    }

    private static <K> Map<K, Integer> merge(Map<K, Integer> map1, Map<K, Integer> map2) {
        Map<K, Integer> result = new HashMap<>();
        if (MapUtils.isNotEmpty(map1)) {
            map1.forEach((k, v) -> result.merge(k, v, Integer::sum));
        }
        if (MapUtils.isNotEmpty(map2)) {
            map2.forEach((k, v) -> result.merge(k, v, Integer::sum));
        }
        return result;
    }
}
