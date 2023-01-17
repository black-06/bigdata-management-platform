package com.bmp.search.service.impl;

import com.bmp.commons.result.Result;
import com.bmp.search.core.SearchRequest;
import com.bmp.search.core.SearchResponse;
import com.bmp.search.core.Searcher;
import com.bmp.search.service.SearchService;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {
    @Override
    public Result<SearchResponse> search(SearchRequest request) {
        return Result.success(Searcher.search(request));
    }
}
