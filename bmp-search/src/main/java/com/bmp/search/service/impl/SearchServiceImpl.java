package com.bmp.search.service.impl;

import com.bmp.commons.result.Result;
import com.bmp.search.core.SearchRequest;
import com.bmp.search.core.SearchResponse;
import com.bmp.search.core.Searcher;
import com.bmp.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final Searcher searcher;

    @Override
    public Result<SearchResponse> search(SearchRequest request) {
        return Result.success(searcher.search(request));
    }
}
