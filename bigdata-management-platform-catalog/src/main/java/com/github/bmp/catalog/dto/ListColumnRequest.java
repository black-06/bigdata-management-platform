package com.github.bmp.catalog.dto;

import com.github.bmp.dao.utils.Paginator;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ListColumnRequest implements ListRequest {
    private Paginator paginator;
    private List<Integer> ids;
    private Integer assetID;
    private List<Integer> assetIDs;
    private Boolean withTags;
}
