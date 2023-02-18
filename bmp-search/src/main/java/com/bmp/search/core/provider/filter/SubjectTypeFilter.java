package com.bmp.search.core.provider.filter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmp.commons.enums.SubjectType;
import com.bmp.dao.utils.BaseEntity;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public class SubjectTypeFilter implements Filter {
    private final boolean disable;
    private final Set<SubjectType> types;

    public SubjectTypeFilter(Collection<SubjectType> types) {
        if (CollectionUtils.isEmpty(types)) {
            this.disable = true;
            this.types = Collections.emptySet();
        } else {
            this.disable = false;
            this.types = EnumSet.copyOf(types);
        }
    }

    @Override
    public <T extends BaseEntity> boolean pushdownToQuery(QueryWrapper<T> query, SubjectType type) {
        if (this.disable || this.types.contains(type)) {
            return true;
        }
        query.apply("1 = 2");
        return true;
    }
}
