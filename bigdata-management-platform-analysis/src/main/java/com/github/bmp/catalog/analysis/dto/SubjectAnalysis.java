package com.github.bmp.catalog.analysis.dto;

import com.github.bmp.commons.enums.DatasourceType;
import com.github.bmp.commons.enums.SubjectType;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
public class SubjectAnalysis {
    private Instant date;
    private Integer collectionCount;
    private Integer datasourceCount;

    /* asset metrics */
    private Integer assetCount;
    private Map<SubjectType, Integer> assetCountGroupByAssetType;
    private Map<DatasourceType, Integer> assetCountGroupByDatasourceType;
    private Map<String, Integer> assetCountGroupByTag;

    private Integer columnCount;
}
