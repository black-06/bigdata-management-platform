package com.bmp.search.service;

import com.bmp.commons.result.Result;
import com.bmp.search.core.SearchRequest;
import com.bmp.search.core.SearchResponse;

public interface SearchService {
    Result<SearchResponse> search(SearchRequest request);
}
