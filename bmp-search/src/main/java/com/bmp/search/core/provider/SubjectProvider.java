package com.bmp.search.core.provider;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.bmp.commons.enums.SubjectType;
import com.bmp.dao.entity.*;
import com.bmp.dao.utils.BaseEntity;
import com.bmp.dao.utils.BaseMapper;
import com.bmp.search.core.Item;
import com.bmp.search.core.SearchRequest;
import com.bmp.search.core.provider.filter.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SubjectProvider implements Provider {

    private final SubjectType type;
    private final List<Filter> extra = new ArrayList<>();

    @Override
    @SuppressWarnings("deprecation")
    public List<Item> recall(SearchRequest request) {
        List<Filter> filters = new ArrayList<>();
        List<SubjectType> types;

        SearchRequest.Filter filter = request.getFilter();
        if (filter != null) {
            types = filter.getSubjectType();
            if (CollectionUtils.isNotEmpty(types) && !types.contains(type)) {
                return Collections.emptyList();
            }

            filters.add(new DatasourceTypeFilter(filter.getDatasourceType()));
            filters.add(new SubjectTypeFilter(filter.getSubjectType()));
            filters.add(new SubjectFilter(filter.getCollection(), filter.getDatasource()));
            filters.add(new TagFilter(filter.getTag()));
            filters.add(new TimeFilter(BaseEntity::getCreateTime, filter.getCreateTime()));
            filters.add(new TimeFilter(BaseEntity::getUpdateTime, filter.getUpdateTime()));
        } else {
            types = Collections.emptyList();
        }
        if (CollectionUtils.isNotEmpty(extra)) {
            filters.addAll(extra);
        }

        String query = request.getQuery();
        switch (type) {
            case COLLECTION:
                return this.applyFilters(filters, types, query, CollectionScore.class);
            case DATASOURCE:
                return this.applyFilters(filters, types, query, DatasourceScore.class);
            case COLUMN:
                return this.applyFilters(filters, types, query, ColumnScore.class);
            case ASSET:
            case DATABASE:
            case TABLE:
            case FILESET:
                return this.applyFilters(filters, types, query, AssetScore.class);
            default:
                throw new IllegalArgumentException("unknown subject type");
        }
    }

    public void addFilter(Filter filter) {
        extra.add(filter);
    }


    private static final List<SubjectType> all_types = Arrays.asList(SubjectType.values());

    private <T extends BaseEntity & SubjectScore> List<Item> applyFilters(
            List<Filter> filters, List<SubjectType> types, String query, Class<T> clazz
    ) {
        QueryWrapper<T> wrapper = Fulltext.buildQueryWrapper(clazz, query);
        final List<Filter> remained = new ArrayList<>();

        types = CollectionUtils.isEmpty(types) ? all_types : types;
        for (Filter filter : filters) {
            for (SubjectType type : types) {
                if (!filter.pushdownToQuery(wrapper, type)) {
                    remained.add(filter);
                }
            }
        }

        return SqlHelper
                .execute(clazz, mapper -> ((BaseMapper<T>) mapper).selectListTo(clazz, wrapper))
                .stream()
                .map(this::BuildItem)
                .filter(item -> {
                    for (Filter filter : remained) {
                        if (!filter.filter(item)) return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    private <T extends SubjectScore> Item BuildItem(T subject) {
        Item item = new Item();
        item.setScore(subject.getScore());
        item.setType(subject.type());
        item.setSubject(subject);
        return item;
    }

    public interface SubjectScore extends Subject {
        @JsonIgnore
        Float getScore();
    }


    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class CollectionScore extends Collection implements SubjectScore {
        @TableField("score")
        private Float score;


    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class DatasourceScore extends Datasource implements SubjectScore {
        @TableField("score")
        private Float score;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @Fulltext.Index("`name`, `description`, `comment`, `details`")
    public static class AssetScore extends Asset implements SubjectScore {
        @TableField("score")
        private Float score;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @Fulltext.Index("`name`, `description`, `comment`")
    public static class ColumnScore extends Column implements SubjectScore {
        @TableField("score")
        private Float score;
    }
}
