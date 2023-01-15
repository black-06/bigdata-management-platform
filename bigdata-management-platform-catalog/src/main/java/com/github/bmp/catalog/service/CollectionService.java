package com.github.bmp.catalog.service;

import com.github.bmp.commons.result.Result;
import com.github.bmp.dao.entity.Collection;
import com.github.bmp.dao.utils.BaseService;

import java.util.List;

public interface CollectionService extends BaseService<Collection> {
    Result<Collection> updateCollection(Collection collection);

    Result<List<Collection>> listCollection();

    Result<?> deleteCollection(int id, boolean deleteDatasource);
}
