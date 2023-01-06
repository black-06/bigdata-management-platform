package org.example.connector.api;

import lombok.Data;

@Data
public class Asset {
    private String name;
    private AssetType type;
    private AssetPath path;
    private Metadata metadata;
    private Column[] columns;
}
