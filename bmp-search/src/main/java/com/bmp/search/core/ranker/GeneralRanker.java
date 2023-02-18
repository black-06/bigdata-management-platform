package com.bmp.search.core.ranker;

import com.bmp.dao.utils.BaseEntity;
import com.bmp.search.core.Item;

/**
 * GeneralRanker is the default ranker.
 * It will first sort items by relevance score, then by create_time
 */
public class GeneralRanker extends TimeRanker implements Ranker {
    public static GeneralRanker Instance = new GeneralRanker();

    public GeneralRanker() {
        super(BaseEntity::getCreateTime);
    }

    @Override
    public int compare(Item o1, Item o2) {
        int compare = Float.compare(o1.getScore(), o2.getScore());
        if (compare == 0) {
            return super.compare(o1, o2);
        }
        return compare;
    }
}
