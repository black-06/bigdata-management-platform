package com.bmp.search.core;

import com.bmp.commons.enums.DatasourceType;
import com.bmp.commons.enums.SubjectType;
import com.bmp.dao.utils.Paginator;
import lombok.Data;

import java.util.List;

@Data
public class SearchRequest {
    private String session;
    private Paginator paginator;
    private String query;
    private Filter filter;
    private SortType sortType;

    @Data
    public static class Filter {
        private List<DatasourceType> datasourceType;
        private List<SubjectType> subjectType;
        private List<Integer> collection;
        private List<Integer> datasource;
        private List<Integer> tag;

        private Period createTime;
        private Period updateTime;
    }
}
