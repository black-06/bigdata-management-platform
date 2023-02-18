package com.bmp.search.core;

import com.bmp.commons.enums.SubjectType;
import com.bmp.dao.entity.*;
import lombok.Data;

import java.util.List;

@Data
public class Item {
    private Float score;
    private SubjectType type;
    private Subject subject;

    private List<Tag> tags;

    /**
     * The collection that the item in.
     */
    private Collection collection;
    /**
     * The datasource that the item in.
     * It will be null If the item is collection.
     */
    private Datasource datasource;
    /**
     * Hierarchical assets. e.g. [database, table].
     * It will be null if the item is collection or datasource.
     */
    private List<Asset> assets;
}
