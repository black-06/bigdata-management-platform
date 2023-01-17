package com.bmp.catalog.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bmp.catalog.dto.ListTagRequest;
import com.bmp.catalog.dto.SubjectID;
import com.bmp.catalog.service.TagService;
import com.bmp.commons.result.Result;
import com.bmp.commons.result.Status;
import com.bmp.dao.entity.Tag;
import com.bmp.dao.entity.TagSubject;
import com.bmp.dao.mapper.TagMapper;
import com.bmp.dao.utils.BaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl extends BaseServiceImpl<TagMapper, Tag> implements TagService {
    private final TagMapper tagMapper;

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
    public Result<Tag> updateTag(Tag tag) {
        return Result.error(Status.INTERNAL_SERVER_ERROR_ARGS, "not implemented");
    }


    @Override
    public Result<?> deleteTag(int id) {
        return Result.error(Status.INTERNAL_SERVER_ERROR_ARGS, "not implemented");
    }


}
