package com.bmp.search.core.provider;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Fulltext<T> extends QueryWrapper<T> {

    public static <T> QueryWrapper<T> buildQueryWrapper(Class<T> clazz, String query) {
        Fulltext<T> fulltext = new Fulltext<>();
        fulltext.setEntityClass(clazz);

        String fields = TableInfoHelper.getTableInfo(clazz).getAllSqlSelect();
        if (StringUtils.isBlank(query)) {
            fulltext.select(fields);
            return fulltext;
        }

        Index annotation = clazz.getAnnotation(Index.class);
        String index = annotation == null ? Index.DefaultValue : annotation.value();
        String param = fulltext.formatParam(null, query);
        String match = String.format("MATCH(%s) AGAINST (%s IN BOOLEAN MODE) as score", index, param);
        fulltext.select(fields, match);
        fulltext.apply(String.format("MATCH(%s) AGAINST (%s IN BOOLEAN MODE)", index, param));
        return fulltext;
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Index {
        String DefaultValue = "`name`, `description`";

        String value();
    }
}