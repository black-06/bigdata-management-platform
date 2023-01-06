package org.example.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.api.dao.entity.Collection;
import org.example.api.dao.mapper.CollectionMapper;
import org.example.api.utils.result.Result;
import org.example.api.service.CollectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {

    private final Logger logger = LoggerFactory.getLogger(CollectionServiceImpl.class);

    private final CollectionMapper collectionMapper;

    @Override
    public Result<?> createCollection(Collection collection) {
        Date now = new Date();
        collection.setId(null);
        collection.setCreateTime(now);
        collection.setUpdateTime(now);
        collectionMapper.insert(collection);
        return Result.success(null);
    }

    @Override
    public Result<List<Collection>> listCollection() {
        List<Collection> collections = collectionMapper.selectList(null);
        return Result.success(collections);
    }
}
