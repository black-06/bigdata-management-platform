package com.bmp.search.controller;

import com.bmp.commons.exceptions.ApiException;
import com.bmp.commons.result.Result;
import com.bmp.commons.result.Status;
import com.bmp.search.core.SearchRequest;
import com.bmp.search.core.SearchResponse;
import com.bmp.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    @ApiException(value = Status.SEARCH_ERROR_ARGS, msg = true)
    public Result<SearchResponse> createTag(@RequestBody SearchRequest request) {
        return searchService.search(request);
    }
}
