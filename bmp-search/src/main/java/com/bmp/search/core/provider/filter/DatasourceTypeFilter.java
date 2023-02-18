package com.bmp.search.core.provider.filter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.bmp.commons.enums.DatasourceType;
import com.bmp.commons.enums.SubjectType;
import com.bmp.dao.entity.Asset;
import com.bmp.dao.entity.Column;
import com.bmp.dao.entity.Datasource;
import com.bmp.dao.mapper.AssetMapper;
import com.bmp.dao.utils.BaseEntity;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class DatasourceTypeFilter implements Filter {
    private final boolean disable;

    private final Collection<DatasourceType> types;
    private final Collection<Integer> assetIDs;

    public DatasourceTypeFilter(Collection<DatasourceType> types) {
        if (CollectionUtils.isEmpty(types)) {
            this.disable = true;
            this.types = Collections.emptyList();
            this.assetIDs = Collections.emptyList();
        } else {
            this.disable = false;
            this.types = types;
            this.assetIDs = SqlHelper
                    .execute(Asset.class, mapper -> ((AssetMapper) mapper).selectJoinList(
                            Asset.class,
                            new MPJLambdaWrapper<Asset>()
                                    .select(Asset::getId)
                                    .innerJoin(Datasource.class, Datasource::getId, Asset::getDatasourceID)
                                    .in(Datasource::getType, this.types)
                    ))
                    .stream()
                    .map(BaseEntity::getId).collect(Collectors.toList());
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public <T extends BaseEntity> boolean pushdownToQuery(QueryWrapper<T> query, SubjectType type) {
        if (disable) {
            return true;
        }
        switch (type) {
            case COLLECTION:
                query.apply("1 = 2");
                return true;
            case DATASOURCE:
                query.lambda().in((SFunction<T, Object>) t -> ((Datasource) t).getType(), types);
                return true;
            case ASSET:
            case DATABASE:
            case TABLE:
            case FILESET:
                query.lambda().in(BaseEntity::getId, assetIDs);
                return true;
            case COLUMN:
                query.lambda().in((SFunction<T, Object>) t -> ((Column) t).getAssetID(), assetIDs);
                return true;
            default:
                throw new IllegalArgumentException("unknown subject type");
        }
    }
}
