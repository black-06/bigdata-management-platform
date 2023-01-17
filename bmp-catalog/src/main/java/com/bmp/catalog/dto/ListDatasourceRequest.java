package com.bmp.catalog.dto;

import com.bmp.commons.enums.DatasourceType;
import com.bmp.dao.utils.Paginator;
import lombok.Data;

import java.util.List;

@Data
public class ListDatasourceRequest implements ListRequest {
    private Paginator paginator;
    private List<Integer> ids;
    private Integer collectionID;
    private DatasourceType type;
    private Boolean withTags;
    private Boolean withAssets;
    private Boolean withAssetTags;
    private Boolean withAssetColumns;
    private Boolean withAssetColumnTags;
}
