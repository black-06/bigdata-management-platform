package com.github.bmp.search.core;

import com.github.bmp.search.core.aggregator.Aggregation;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchResponse {
    private List<Item> items;
    private Aggregation aggregation;
    private int total;
}