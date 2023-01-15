package com.bmp.catalog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bmp.catalog.dto.ListAssetRequest;
import com.bmp.catalog.vo.AssetView;
import com.bmp.commons.result.Result;
import com.bmp.dao.entity.Asset;
import com.bmp.dao.utils.BaseService;

public interface AssetService extends BaseService<Asset> {
    Result<Asset> updateAsset(Asset asset);

    IPage<AssetView> listAsset(ListAssetRequest request);

    Result<?> deleteAsset(int id);
}
