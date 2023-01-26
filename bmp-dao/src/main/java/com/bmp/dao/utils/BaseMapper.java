package com.bmp.dao.utils;

import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import com.github.yulichang.base.MPJBaseMapper;

import java.util.List;

/**
 * Add batch insert function, only support MySQL. see {@link InsertBatchSomeColumn}
 */
public interface BaseMapper<T> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T>, MPJBaseMapper<T> {
    @SuppressWarnings("UnusedReturnValue")
    int insertBatchSomeColumn(List<T> entityList);
}
