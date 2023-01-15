package com.bmp.catalog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bmp.catalog.dto.ListAssetRequest;
import com.bmp.catalog.service.AssetService;
import com.bmp.catalog.vo.AssetView;
import com.bmp.commons.exceptions.ApiException;
import com.bmp.commons.result.Result;
import com.bmp.commons.result.Status;
import com.bmp.dao.entity.Asset;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("asset")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;


    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiException(value = Status.UPDATE_ERROR_ARGS, args = {"asset"})
    public Result<Asset> updateAsset(
            @PathVariable("id") int id, @RequestBody Asset asset
    ) {
        asset.setId(id);
        return assetService.updateAsset(asset);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiException(value = Status.QUERY_ERROR_ARGS, args = {"asset"})
    public Result<Asset> queryAsset(@PathVariable("id") int id) {
        return Result.success(assetService.getById(id));
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @ApiException(value = Status.QUERY_ERROR_ARGS, args = {"asset"})
    public Result<IPage<AssetView>> queryAssetList(ListAssetRequest request) {
        return Result.success(assetService.listAsset(request));
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiException(value = Status.DELETE_ERROR_ARGS, args = {"asset"})
    public Result<?> deleteAsset(@PathVariable("id") int id) {
        return assetService.deleteAsset(id);
    }
}
