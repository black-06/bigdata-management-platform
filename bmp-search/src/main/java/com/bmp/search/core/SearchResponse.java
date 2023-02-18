package com.bmp.search.core;

import com.bmp.search.core.aggregator.Aggregation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {
    private List<Item> items;
    private int total;
    private Aggregation aggregation;
    private SortType sortType;
}