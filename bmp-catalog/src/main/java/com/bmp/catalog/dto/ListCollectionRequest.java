package com.bmp.catalog.dto;

import com.bmp.dao.utils.Paginator;
import lombok.Data;

import java.util.List;

@Data
public class ListCollectionRequest implements ListRequest {
    private Paginator paginator;
    private List<Integer> ids;
}
