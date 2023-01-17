package com.bmp.catalog.controller;

import com.bmp.catalog.dto.RefreshRequest;
import com.bmp.catalog.service.MetaSyncService;
import com.bmp.commons.exceptions.ApiException;
import com.bmp.commons.result.Result;
import com.bmp.commons.result.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("meta_sync")
@RequiredArgsConstructor
public class MetaSyncController {

    private final MetaSyncService metaSyncService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiException(value = Status.META_SYNC_ERROR_ARGS, msg = true)
    public Result<?> refresh(@RequestBody RefreshRequest request) {
        return metaSyncService.refresh(request);
    }
}
