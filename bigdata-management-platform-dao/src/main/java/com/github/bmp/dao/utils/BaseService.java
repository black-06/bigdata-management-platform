package com.github.bmp.dao.utils;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface BaseService<T extends BaseEntity> extends IService<T> {
    T insert(T entity);

    List<T> insertBatch(List<T> list);
}
