package com.bmp.catalog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.bmp.catalog.dto.SubjectID;
import com.bmp.catalog.dto.UpdateTagSubjectRequest;
import com.bmp.catalog.service.TagSubjectService;
import com.bmp.catalog.vo.TagView;
import com.bmp.commons.enums.SubjectType;
import com.bmp.commons.result.Result;
import com.bmp.dao.entity.Asset;
import com.bmp.dao.entity.Tag;
import com.bmp.dao.entity.TagSubject;
import com.bmp.dao.mapper.TagSubjectMapper;
import com.bmp.dao.utils.BaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagSubjectServiceImpl extends BaseServiceImpl<TagSubjectMapper, TagSubject> implements TagSubjectService {
    private final TagSubjectMapper subjectMapper;

    public Map<SubjectID, List<TagView>> listTagSubject(List<SubjectID> subjectIDs) {
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
                        Collectors.mapping(
                                dto -> new TagView(dto.getId(), dto.getName()),
                                Collectors.toList()
                        )
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

    private boolean isAsset(TagSubject subject) {
        SubjectType subjectType = subject.getSubjectType();
        return subjectType == SubjectType.ASSET ||
                subjectType == SubjectType.DATABASE ||
                subjectType == SubjectType.TABLE ||
                subjectType == SubjectType.FILESET;
    }

    @Transactional
    public void batchCreate(List<TagSubject> subjects) {
        List<Integer> assetIDs = subjects.stream()
                .filter(this::isAsset)
                .map(TagSubject::getSubjectID)
                .collect(Collectors.toList());
        List<Asset> assets = Db.listByIds(assetIDs, Asset.class);
        Map<Integer, SubjectType> assetTypes = new HashMap<>();
        for (Asset asset : assets) {
            assetTypes.put(asset.getId(), asset.type());
        }

        Instant now = Instant.now();
        HashSet<TagSubject> creates = new HashSet<>(subjects);

        LambdaQueryWrapper<TagSubject> condition = new LambdaQueryWrapper<>();
        for (TagSubject create : creates) {
            SubjectType type = isAsset(create) ? assetTypes.get(create.getSubjectID()) : create.getSubjectType();
            condition = condition.or(sub -> sub
                    .eq(TagSubject::getTagID, create.getTagID())
                    .eq(TagSubject::getSubjectID, create.getSubjectID())
                    .eq(TagSubject::getSubjectType, type)
            );
            // modify asset type
            create.setCreateTime(now).setUpdateTime(now).setSubjectType(type);
        }
        // remove duplicates
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

    @Override
    public void detachSubject(List<SubjectID> subjects) {
        LambdaQueryWrapper<TagSubject> condition = new LambdaQueryWrapper<>();
        for (SubjectID detach : subjects) {
            condition = condition.or(sub -> sub
                    .eq(TagSubject::getSubjectID, detach.getId())
                    .eq(TagSubject::getSubjectType, detach.getType())
            );
        }
        subjectMapper.delete(condition);
    }
}
