package com.bmp.catalog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmp.catalog.dto.ListCollectionRequest;
import com.bmp.catalog.service.CollectionService;
import com.bmp.commons.result.Result;
import com.bmp.commons.result.Status;
import com.bmp.dao.entity.Collection;
import com.bmp.dao.mapper.CollectionMapper;
import com.bmp.dao.utils.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CollectionServiceImpl extends BaseServiceImpl<CollectionMapper, Collection> implements CollectionService {

    private final Logger logger = LoggerFactory.getLogger(CollectionServiceImpl.class);

    private final CollectionMapper collectionMapper;

    @Override
    public Result<Collection> createCollection(Collection collection) {
        Instant now = Instant.now();
        collection.setId(null);
        collection.setCreateTime(now);
        collection.setUpdateTime(now);
        collectionMapper.insert(collection);
        return Result.success(collection);
    }

    @Override
    public Result<Collection> updateCollection(Collection update) {
        Integer id = update.getId();
        if (id == null || id == 0) {
            return Result.error(Status.INVALID_PARAM_ARGS, "id");
        }
        Collection collection = collectionMapper.selectById(id);
        collection.setUpdateTime(Instant.now());
        String name = update.getName();
        if (StringUtils.isNotBlank(name)) {
            collection.setName(name);
        }
        String description = update.getDescription();
        if (StringUtils.isNotBlank(description)) {
            collection.setDescription(description);
        }

        collectionMapper.updateById(collection);
        return Result.success(collection);
    }

    @Override
    public Result<IPage<Collection>> listCollection(ListCollectionRequest request) {
        // query collections with conditions lambda.
        Page<Collection> collections = collectionMapper.selectPage(request.getPage(), new QueryWrapper<Collection>()
                .lambda()
                .in(Collection::getId, request.getIds())
        );
        return Result.success(collections);
    }

    @Override
    public Result<?> deleteCollection(int id, boolean deleteDatasource) {
        collectionMapper.deleteById(id);
        return Result.success(null);
    }
}
