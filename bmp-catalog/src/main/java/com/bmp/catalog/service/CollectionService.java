package com.bmp.catalog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bmp.catalog.dto.ListCollectionRequest;
import com.bmp.commons.result.Result;
import com.bmp.dao.entity.Collection;
import com.bmp.dao.utils.BaseService;

public interface CollectionService extends BaseService<Collection> {
    Result<Collection> createCollection(Collection collection);

    Result<Collection> updateCollection(Collection collection);

    Result<IPage<Collection>> listCollection(ListCollectionRequest request);

    Result<?> deleteCollection(int id, boolean deleteDatasource);
}
