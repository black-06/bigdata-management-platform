package com.github.bmp.search.core.provider;

import com.github.bmp.search.core.Item;
import com.github.bmp.search.core.SearchRequest;

import java.util.List;

/**
 * It recalls search items from resources.
 */
public interface Provider {
    List<Item> recall(SearchRequest request);
}
