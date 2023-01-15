package com.github.bmp.catalog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.bmp.catalog.dto.SubjectID;
import com.github.bmp.catalog.dto.UpdateTagSubjectRequest;
import com.github.bmp.catalog.service.TagSubjectService;
import com.github.bmp.commons.enums.SubjectType;
import com.github.bmp.commons.result.Result;
import com.github.bmp.dao.entity.Tag;
import com.github.bmp.dao.entity.TagSubject;
import com.github.bmp.dao.mapper.TagSubjectMapper;
import com.github.bmp.dao.utils.BaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagSubjectServiceImpl extends BaseServiceImpl<TagSubjectMapper, TagSubject> implements TagSubjectService {
    private final TagSubjectMapper subjectMapper;

    public Map<SubjectID, List<Tag>> listTagSubject(List<SubjectID> subjectIDs) {
        MPJLambdaWrapper<TagSubject> query = new MPJLambdaWrapper<TagSubject>()
                .selectAll(Tag.class)
                .select(TagSubject::getSubjectID, TagSubject::getSubjectType)
                .innerJoin(Tag.class, Tag::getId, TagSubject::getTagID);
        for (SubjectID subjectID : subjectIDs) {
            query = query.or(sub -> sub
                    .eq(TagSubject::getSubjectID, subjectID.getId())
                    .eq(TagSubject::getSubjectType, subjectID.getType())
            );
        }
        return subjectMapper.selectJoinList(TagSubjectDTO.class, query)
                .stream()
                .collect(Collectors.groupingBy(
                        dto -> new SubjectID(dto.subjectID, dto.subjectType),
                        Collectors.mapping(dto -> dto, Collectors.toList())
                ));
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class TagSubjectDTO extends Tag {
        private Integer subjectID;
        private SubjectType subjectType;
    }

    @Override
    public Result<?> updateTagSubject(UpdateTagSubjectRequest request) {
        if (CollectionUtils.isNotEmpty(request.getDetach())) {
            batchDelete(request.getDetach());
        }
        if (CollectionUtils.isNotEmpty(request.getBind())) {
            batchCreate(request.getBind());
        }
        return Result.success(null);
    }

    @Transactional
    public void batchCreate(List<TagSubject> subjects) {
        Instant now = Instant.now();
        HashSet<TagSubject> creates = new HashSet<>(subjects);
        LambdaQueryWrapper<TagSubject> condition = new LambdaQueryWrapper<>();
        for (TagSubject create : creates) {
            condition = condition.or(sub -> sub
                    .eq(TagSubject::getTagID, create.getTagID())
                    .eq(TagSubject::getSubjectID, create.getSubjectID())
                    .eq(TagSubject::getSubjectType, create.getSubjectType())
            );
            create.setCreateTime(now).setUpdateTime(now);
        }
        subjectMapper.selectList(condition).forEach(creates::remove);
        if (creates.isEmpty()) {
            return;
        }
        subjectMapper.insertBatchSomeColumn(new ArrayList<>(creates));
    }

    private void batchDelete(List<TagSubject> subjects) {
        LambdaQueryWrapper<TagSubject> condition = new LambdaQueryWrapper<>();
        for (TagSubject detach : subjects) {
            condition = condition.or(sub -> sub
                    .eq(TagSubject::getTagID, detach.getTagID())
                    .eq(TagSubject::getSubjectID, detach.getSubjectID())
                    .eq(TagSubject::getSubjectType, detach.getSubjectType())
            );
        }
        subjectMapper.delete(condition);
    }
}
