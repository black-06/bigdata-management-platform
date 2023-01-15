package com.bmp.connector.api.nested;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

public class NestedInfo {
    private int parentID;
    @JsonIgnore
    private String parentName;
    private Map<String, Integer> types;
    private int typeCount;
    private int appearCount;
    private int rowCount;
}
