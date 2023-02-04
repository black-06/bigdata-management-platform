package com.bmp.catalog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bmp.catalog.dto.ListAssetRequest;
import com.bmp.catalog.vo.AssetView;
import com.bmp.commons.result.Result;
import com.bmp.dao.entity.Asset;
import com.bmp.dao.utils.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AssetService extends BaseService<Asset> {
    Result<Asset> updateAsset(Asset asset);

    IPage<AssetView> listAsset(ListAssetRequest request);

    @Transactional
    void batchDeleteAsset(List<Integer> datasourceIDs);
}
