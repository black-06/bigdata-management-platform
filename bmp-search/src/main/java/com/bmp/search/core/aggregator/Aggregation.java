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
        if (MapUtils.isEmpty(this.datasourceType)) {
            this.datasourceType = new HashMap<>();
        }
        if (MapUtils.isNotEmpty(other.datasourceType)) {
            this.datasourceType.putAll(other.datasourceType);
        }

        if (MapUtils.isEmpty(this.subjectType)) {
            this.subjectType = new HashMap<>();
        }
        if (MapUtils.isNotEmpty(other.subjectType)) {
            this.subjectType.putAll(other.subjectType);
        }

        if (MapUtils.isEmpty(this.collection)) {
            this.collection = new HashMap<>();
        }
        if (MapUtils.isNotEmpty(other.collection)) {
            this.collection.putAll(other.collection);
        }

        if (MapUtils.isEmpty(this.datasource)) {
            this.datasource = new HashMap<>();
        }
        if (MapUtils.isNotEmpty(other.datasource)) {
            this.datasource.putAll(other.datasource);
        }

        if (MapUtils.isEmpty(this.tag)) {
            this.tag = new HashMap<>();
        }
        if (MapUtils.isNotEmpty(other.tag)) {
            this.tag.putAll(other.tag);
        }
    }
}
