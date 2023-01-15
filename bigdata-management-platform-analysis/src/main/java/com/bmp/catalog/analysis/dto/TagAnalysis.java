package com.bmp.catalog.analysis.dto;

import com.bmp.dao.entity.Tag;
import lombok.Data;

import java.util.List;

@Data
public class TagAnalysis {
    private Integer tagCount;
    /**
     * It's sorted by used_count.
     */
    private List<TagCount> topTags;

    @Data
    public static class TagCount {
        private Tag tag;
        private int used_count;
    }
}
