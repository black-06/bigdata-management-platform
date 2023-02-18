package com.bmp.search.core.provider;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.bmp.commons.enums.SubjectType;
import com.bmp.dao.dto.SubjectID;
import com.bmp.dao.entity.Tag;
import com.bmp.dao.entity.TagSubject;
import com.bmp.dao.utils.BaseMapper;
import com.bmp.search.core.Item;
import com.bmp.search.core.SearchRequest;
import com.bmp.search.core.provider.filter.IDFilter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TagProvider implements Provider {
    public static final TagProvider Instance = new TagProvider();

    @Override
    public List<Item> recall(SearchRequest request) {
        List<TagScore> tags = SqlHelper.execute(
                TagScore.class,
                mapper -> ((BaseMapper<TagScore>) mapper).selectListTo(
                        TagScore.class,
                        Fulltext.buildQueryWrapper(TagScore.class, request.getQuery())
                )
        );

        List<Integer> tagIDs = new ArrayList<>();
        Map<Integer, Float> tagScores = new HashMap<>();
        for (TagScore tag : tags) {
            tagIDs.add(tag.getId());
            tagScores.put(tag.getId(), tag.getScore());
        }
        if (CollectionUtils.isEmpty(tagIDs)) {
            return Collections.emptyList();
        }
        Map<SubjectID, Float> subjectScores = new HashMap<>();
        Map<SubjectType, List<Integer>> subjectIDs = SimpleQuery.group(
                new LambdaQueryWrapper<TagSubject>().in(TagSubject::getTagID, tagIDs),
                TagSubject::getSubjectType,
                Collectors.mapping(tagSubject -> {
                    subjectScores.put(SubjectID.of(tagSubject), tagScores.get(tagSubject.getTagID()));
                    return tagSubject.getSubjectID();
                }, Collectors.toList())
        );

        Map<SubjectID, Item> items = new HashMap<>();
        request.setQuery(null);
        subjectIDs.forEach((type, ids) -> {
            SubjectProvider provider = new SubjectProvider(type);
            provider.addFilter(new IDFilter(ids));
            provider.recall(request).forEach(item -> {
                SubjectID id = SubjectID.of(item.getSubject());
                item.setScore(subjectScores.get(id));
                items.put(id, item);
            });
        });
        return new ArrayList<>(items.values());
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class TagScore extends Tag {
        @TableField("score")
        private Float score;
    }
}
