package com.bmp.search.core.provider.filter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmp.commons.enums.SubjectType;
import com.bmp.dao.utils.BaseEntity;
import com.bmp.search.core.Item;
import com.bmp.search.core.provider.Provider;

/**
 * It is used with {@link Provider} to filter recalled items.
 * providers check whether the {@link Filter} can push down to query,
 * if not, it will {@link #filter} items one by one.
 */
public interface Filter {
    /**
     * filter one item at one time.
     * It can be used to iterate each item in slice usually.
     */
    default boolean filter(Item item) {
        throw new UnsupportedOperationException();
    }

    /**
     * push filter to sql query to speed up and reduce the amount of data recalled from db.
     * It will return the query clause string as first result.
     * It can't be pushed down when result is a blank str.
     */
    <T extends BaseEntity> boolean pushdownToQuery(QueryWrapper<T> query, SubjectType type);
}