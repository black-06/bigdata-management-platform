package com.bmp.search.core.packer;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.bmp.dao.dto.SubjectID;
import com.bmp.dao.dto.TagSubjectDTO;
import com.bmp.dao.entity.Tag;
import com.bmp.dao.entity.TagSubject;
import com.bmp.dao.mapper.TagSubjectMapper;
import com.bmp.search.core.Item;
import com.github.yulichang.wrapper.MPJLambdaWrapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TagPacker implements Packer {
    @Override
    public List<Item> pack(List<Item> items) {
        MPJLambdaWrapper<TagSubject> query = buildQuery(items
                .stream()
                .map(item -> SubjectID.of(item.getSubject()))
                .collect(Collectors.toList()));

        Map<SubjectID, List<Tag>> tags = SqlHelper
                .execute(
                        TagSubject.class,
                        mapper -> ((TagSubjectMapper) mapper).selectJoinList(TagSubjectDTO.class, query)
                )
                .stream()
                .collect(Collectors.groupingBy(
                        dto -> new SubjectID(dto.getSubjectID(), dto.getSubjectType()),
                        Collectors.mapping(dto -> (Tag) dto, Collectors.toList())
                ));

        for (Item item : items) {
            item.setTags(tags.getOrDefault(SubjectID.of(item.getSubject()), Collections.emptyList()));
        }
        return items;
    }

    private MPJLambdaWrapper<TagSubject> buildQuery(List<SubjectID> subjectIDs) {
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
        return query;
    }
}
