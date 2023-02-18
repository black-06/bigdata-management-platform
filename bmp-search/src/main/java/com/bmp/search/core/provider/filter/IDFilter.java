package com.bmp.search.core.provider.filter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmp.commons.enums.SubjectType;
import com.bmp.dao.utils.BaseEntity;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

@RequiredArgsConstructor
public class IDFilter implements Filter {

    private final List<Integer> ids;

    @Override
    public <T extends BaseEntity> boolean pushdownToQuery(QueryWrapper<T> query, SubjectType type) {
        if (CollectionUtils.isEmpty(ids)) {
            return true;
        }
        query.lambda().in(BaseEntity::getId, ids);
        return true;
    }
}
