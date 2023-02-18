package com.bmp.search.core.ranker;

import com.bmp.dao.utils.BaseEntity;
import com.bmp.search.core.Item;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.function.Function;

/**
 * TimeRanker will sort by time field, e.g. create_time, update_time.
 */
@RequiredArgsConstructor
public class TimeRanker implements Ranker {
    public static TimeRanker CreateTimeInstance = new TimeRanker(BaseEntity::getCreateTime);
    public static TimeRanker UpdateTimeInstance = new TimeRanker(BaseEntity::getUpdateTime);

    private final Function<BaseEntity, Instant> column;

    @Override
    public int compare(Item o1, Item o2) {
        Instant instant1 = column.apply((BaseEntity) o1.getSubject());
        Instant instant2 = column.apply((BaseEntity) o2.getSubject());
        return instant1.compareTo(instant2);
    }
}
