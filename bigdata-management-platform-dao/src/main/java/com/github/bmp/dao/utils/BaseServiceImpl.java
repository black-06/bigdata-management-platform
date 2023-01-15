package com.github.bmp.dao.utils;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.Instant;
import java.util.List;

public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements BaseService<T> {

    public T insert(T entity) {
        Instant now = Instant.now();
        entity.setId(null);
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        super.getBaseMapper().insert(entity);
        return entity;
    }

    @Override
    public List<T> insertBatch(List<T> list) {
        Instant now = Instant.now();
        list.forEach(entity -> {
            entity.setId(null);
            entity.setCreateTime(now);
            entity.setUpdateTime(now);
        });
        super.getBaseMapper().insertBatchSomeColumn(list);
        return list;
    }
}
