package com.github.bmp.catalog.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.github.bmp.connector.api.list.AssetPath;
import lombok.Data;

import java.util.List;

@Data
public class RefreshRequest {
    @JsonAlias("datasource_id")
    private int datasourceID;

    private List<AssetPath> paths;

    private boolean async;
}
