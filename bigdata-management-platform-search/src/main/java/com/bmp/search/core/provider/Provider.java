package com.bmp.search.core.provider;

import com.bmp.search.core.Item;
import com.bmp.search.core.SearchRequest;

import java.util.List;

/**
 * It recalls search items from resources.
 */
public interface Provider {
    List<Item> recall(SearchRequest request);
}
