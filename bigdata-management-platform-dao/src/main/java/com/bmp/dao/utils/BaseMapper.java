package com.bmp.dao.utils;

import com.github.yulichang.base.MPJBaseMapper;

import java.util.List;

public interface BaseMapper<T> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T>, MPJBaseMapper<T> {
    @SuppressWarnings("UnusedReturnValue")
    int insertBatchSomeColumn(List<T> entityList);
}
