package com.bmp.search.core.provider.filter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmp.commons.enums.SubjectType;
import com.bmp.dao.utils.BaseEntity;
import com.bmp.search.core.Period;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public class TimeFilter implements Filter {
    private final Function<BaseEntity, ?> column;
    private final Period period;

    @Override
    public <T extends BaseEntity> boolean pushdownToQuery(QueryWrapper<T> query, SubjectType type) {
        if (period == null) {
            return true;
        }
        if (period.getStart() != null) {
            query.lambda().ge(column::apply, period.getStart());
        }
        if (period.getEnd() != null) {
            query.lambda().le(column::apply, period.getEnd());
        }
        return true;
    }
}
