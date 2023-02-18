package com.bmp.dao.utils;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.github.yulichang.base.MPJBaseMapper;
import com.github.yulichang.toolkit.Constant;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseMapper<T> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T>, MPJBaseMapper<T> {
    @SuppressWarnings("UnusedReturnValue")
    int insertBatchSomeColumn(List<T> entityList);

    <DTO> List<DTO> selectListTo(@Param(Constant.CLAZZ) Class<DTO> clazz, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
}
