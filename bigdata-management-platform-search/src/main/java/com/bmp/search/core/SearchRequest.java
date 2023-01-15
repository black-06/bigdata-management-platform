package com.bmp.search.core;

import com.bmp.commons.enums.DatasourceType;
import com.bmp.commons.enums.SubjectType;
import com.bmp.dao.utils.Paginator;
import lombok.Data;
import org.apache.commons.lang3.Range;

import java.time.Duration;
import java.time.Instant;

@Data
public class SearchRequest {
    private Paginator paginator;
    private String query;
    private Filter filter;
    private SortType sortType;

    public enum SortType {
        /**
         * GENERAL means sort by text relevance first.
         */
        GENERAL,
        /**
         * CREATE_TIME means sort by create_time of entity.
         */
        CREATE_TIME,
        /**
         * UPDATE_TIME means sort by update_time of entity.
         */
        UPDATE_TIME,
    }

    @Data
    public static class Filter {
        private DatasourceType[] datasourceType;
        private SubjectType[] subjectType;
        private int[] collection;
        private int[] datasource;
        private int[] tag;

        private Period createTime;
        private Period updateTime;
    }

    /**
     * Period is a length of time used to filter search result.
     * It's either defined by start time and end time or a time duration.
     * {@link #duration} has higher priority compared to {@link #range}.
     */
    @Data
    public static class Period {
        private Range<Instant> range;
        private Duration duration;
    }
}
