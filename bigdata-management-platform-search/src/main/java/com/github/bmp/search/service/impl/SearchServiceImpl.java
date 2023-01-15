package com.github.bmp.search.service.impl;

import com.github.bmp.commons.result.Result;
import com.github.bmp.search.core.SearchRequest;
import com.github.bmp.search.core.SearchResponse;
import com.github.bmp.search.core.Searcher;
import com.github.bmp.search.service.SearchService;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {
    @Override
    public Result<SearchResponse> search(SearchRequest request) {
        return Result.success(Searcher.search(request));
    }
}
