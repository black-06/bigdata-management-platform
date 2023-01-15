package com.bmp.catalog.dto;

import com.bmp.commons.enums.AssetType;
import com.bmp.dao.utils.Paginator;
import lombok.Data;

import java.util.List;

@Data
public class ListAssetRequest implements ListRequest {
    private Paginator paginator;
    private List<Integer> ids;
    private Integer parentID;
    private Integer datasourceID;
    private List<Integer> datasourceIDs;
    private AssetType type;
    private Boolean withTags;
    private Boolean withColumns;
    private Boolean withColumnTags;
}
