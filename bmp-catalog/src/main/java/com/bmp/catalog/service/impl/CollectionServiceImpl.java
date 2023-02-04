package com.bmp.catalog.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bmp.catalog.service.CollectionService;
import com.bmp.catalog.service.DatasourceService;
import com.bmp.commons.result.Result;
import com.bmp.commons.result.Status;
import com.bmp.dao.entity.Collection;
import com.bmp.dao.entity.Datasource;
import com.bmp.dao.mapper.CollectionMapper;
import com.bmp.dao.utils.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionServiceImpl extends BaseServiceImpl<CollectionMapper, Collection> implements CollectionService {

    private final Logger logger = LoggerFactory.getLogger(CollectionServiceImpl.class);

    private final CollectionMapper collectionMapper;
    private final DatasourceService datasourceService;

    @Override
    @SuppressWarnings("DuplicatedCode")
    public Result<Collection> updateCollection(Collection update) {
        Integer id = update.getId();
        if (id == null || id == 0) {
            return Result.error(Status.INVALID_PARAM_ARGS, "id");
        }
        Collection collection = collectionMapper.selectById(id);
        if (collection == null) {
            return Result.error(Status.RESOURCE_NOTFOUND_ARGS, "collection id=", id);
        }
        collection.setUpdateTime(Instant.now());
        if (StringUtils.isNotBlank(update.getName())) {
            collection.setName(update.getName());
        }
        if (StringUtils.isNotBlank(update.getDescription())) {
            collection.setDescription(update.getDescription());
        }
        collectionMapper.updateById(collection);
        return Result.success(collection);
    }

    @Override
    public Result<List<Collection>> listCollection() {
        List<Collection> collections = collectionMapper.selectList(null);
        return Result.success(collections);
    }

    @Override
    @Transactional
    public Result<?> deleteCollection(int id, boolean deleteDatasource) {
        collectionMapper.deleteById(id);
        if (deleteDatasource) {
            datasourceService.batchDeleteDatasource(Collections.singletonList(id));
        } else {
            datasourceService.getBaseMapper().update(null, new LambdaUpdateWrapper<Datasource>()
                    .eq(Datasource::getCollectionID, id)
                    .set(Datasource::getCollectionID, 0)
            );
        }
        return Result.success(null);
    }
}
