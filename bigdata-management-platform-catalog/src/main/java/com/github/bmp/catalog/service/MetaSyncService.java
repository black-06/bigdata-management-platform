package com.github.bmp.catalog.service;

import com.github.bmp.catalog.dto.RefreshRequest;
import com.github.bmp.commons.result.Result;

public interface MetaSyncService {
    Result<?> refresh(RefreshRequest request);
}
