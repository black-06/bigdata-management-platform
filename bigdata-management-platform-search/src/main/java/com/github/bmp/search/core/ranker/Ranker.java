package com.github.bmp.search.core.ranker;

import com.github.bmp.search.core.Item;

import java.util.List;

/**
 * rank search items based on text relevance.
 */
public interface Ranker {
    List<Item> rank(List<Item> items);
}
