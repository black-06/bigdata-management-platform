package com.bmp.catalog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bmp.catalog.dto.ListTagRequest;
import com.bmp.catalog.dto.SubjectID;
import com.bmp.catalog.service.TagService;
import com.bmp.commons.result.Result;
import com.bmp.commons.result.Status;
import com.bmp.dao.entity.Tag;
import com.bmp.dao.entity.TagSubject;
import com.bmp.dao.mapper.TagMapper;
import com.bmp.dao.mapper.TagSubjectMapper;
import com.bmp.dao.utils.BaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl extends BaseServiceImpl<TagMapper, Tag> implements TagService {
    private final TagMapper tagMapper;
    private final TagSubjectMapper subjectMapper;

    @Override
    public Result<IPage<Tag>> listTag(ListTagRequest request) {
        MPJLambdaWrapper<Tag> query = new MPJLambdaWrapper<>();
        if (CollectionUtils.isNotEmpty(request.getIds())) {
            query = query.in(Tag::getId, request.getIds());
        }
        if (CollectionUtils.isNotEmpty(request.getSubjectIDs())) {
            query = buildInnerJoin(query, request.getSubjectIDs());
        }
        return Result.success(tagMapper.selectJoinPage(request.getPage(), Tag.class, query));
    }

    private MPJLambdaWrapper<Tag> buildInnerJoin(MPJLambdaWrapper<Tag> query, List<SubjectID> subjectIDs) {
        return query.innerJoin(TagSubject.class, TagSubject::getTagID, Tag::getId)
                .and(sub -> {
                    for (SubjectID subjectID : subjectIDs) {
                        sub.or().eq(TagSubject::getSubjectID, subjectID.getId());
                    }
                });
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    public Result<Tag> updateTag(Tag update) {
        Integer id = update.getId();
        if (id == null || id == 0) {
            return Result.error(Status.INVALID_PARAM_ARGS, "id");
        }
        Tag tag = tagMapper.selectById(id);
        if (tag == null) {
            return Result.error(Status.RESOURCE_NOTFOUND_ARGS, "tag id=", id);
        }
        tag.setUpdateTime(Instant.now());
        if (StringUtils.isNotBlank(update.getName())) {
            tag.setName(update.getName());
        }
        if (StringUtils.isNotBlank(update.getDescription())) {
            tag.setDescription(update.getDescription());
        }
        tagMapper.updateById(tag);
        return Result.success(tag);
    }


    @Override
    public Result<?> deleteTag(int id) {
        long count = subjectMapper.selectCount(new LambdaQueryWrapper<TagSubject>().eq(TagSubject::getTagID, id));
        if (count > 0) {
            return Result.error(Status.IN_USE_ERROR_ARGS, "tag");
        }
        return Result.success(null);
    }
}
