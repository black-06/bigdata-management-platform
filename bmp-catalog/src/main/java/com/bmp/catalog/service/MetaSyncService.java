package com.bmp.catalog.service;

import com.bmp.catalog.dto.RefreshRequest;
import com.bmp.commons.result.Result;

public interface MetaSyncService {
    Result<?> refresh(RefreshRequest request);
}
