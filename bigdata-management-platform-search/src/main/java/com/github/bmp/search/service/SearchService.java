package com.github.bmp.search.service;

import com.github.bmp.commons.result.Result;
import com.github.bmp.search.core.SearchRequest;
import com.github.bmp.search.core.SearchResponse;

public interface SearchService {
    Result<SearchResponse> search(SearchRequest request);
}
