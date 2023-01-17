package com.bmp.catalog.service;

import com.bmp.commons.result.Result;
import com.bmp.dao.entity.Collection;
import com.bmp.dao.utils.BaseService;

import java.util.List;

public interface CollectionService extends BaseService<Collection> {
    Result<Collection> updateCollection(Collection collection);

    Result<List<Collection>> listCollection();

    Result<?> deleteCollection(int id, boolean deleteDatasource);
}
