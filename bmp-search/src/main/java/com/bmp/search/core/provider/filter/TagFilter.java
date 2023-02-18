package com.bmp.search.core.provider.filter;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.bmp.commons.enums.SubjectType;
import com.bmp.dao.entity.TagSubject;
import com.bmp.dao.utils.BaseEntity;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

public class TagFilter implements Filter {
    private final boolean disable;
    private final Map<SubjectType, List<Integer>> subjectIDs;

    public TagFilter(Collection<Integer> tagIDs) {
        if (CollectionUtils.isEmpty(tagIDs)) {
            this.disable = true;
            this.subjectIDs = Collections.emptyMap();
        } else {
            this.disable = false;
            this.subjectIDs = Db
                    .list(new LambdaQueryWrapper<TagSubject>().in(TagSubject::getTagID, tagIDs))
                    .stream()
                    .collect(Collectors.groupingBy(
                            TagSubject::getSubjectType,
                            () -> new EnumMap<>(SubjectType.class),
                            Collectors.mapping(TagSubject::getTagID, Collectors.toList())
                    ));
        }
    }

    @Override
    public <T extends BaseEntity> boolean pushdownToQuery(QueryWrapper<T> query, SubjectType type) {
        if (disable) {
            return true;
        }
        List<Integer> subjectIDs = this.subjectIDs.get(type);
        if (CollectionUtils.isEmpty(subjectIDs)) {
            query.apply("1 = 0");
            return true;
        }
        query.lambda().in(BaseEntity::getId, subjectIDs);
        return true;
    }
}
