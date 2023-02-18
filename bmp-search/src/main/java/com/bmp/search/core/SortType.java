package com.bmp.search.core;

import com.bmp.search.core.ranker.GeneralRanker;
import com.bmp.search.core.ranker.Ranker;
import com.bmp.search.core.ranker.TimeRanker;

import java.util.List;

public enum SortType {
    /**
     * GENERAL means sort by text relevance first.
     */
    GENERAL(GeneralRanker.Instance),
    /**
     * CREATE_TIME means sort by create_time of entity.
     */
    CREATE_TIME(TimeRanker.CreateTimeInstance),
    /**
     * UPDATE_TIME means sort by update_time of entity.
     */
    UPDATE_TIME(TimeRanker.UpdateTimeInstance),
    ;

    private final Ranker ranker;

    SortType(Ranker ranker) {
        this.ranker = ranker;
    }

    public void sort(List<Item> items) {
        items.sort(ranker);
    }
}
