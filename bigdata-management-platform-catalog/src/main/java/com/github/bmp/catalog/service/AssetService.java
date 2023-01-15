package com.github.bmp.catalog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.bmp.catalog.dto.ListAssetRequest;
import com.github.bmp.catalog.vo.AssetView;
import com.github.bmp.commons.result.Result;
import com.github.bmp.dao.entity.Asset;
import com.github.bmp.dao.utils.BaseService;

public interface AssetService extends BaseService<Asset> {
    Result<Asset> updateAsset(Asset asset);

    IPage<AssetView> listAsset(ListAssetRequest request);

    Result<?> deleteAsset(int id);
}
