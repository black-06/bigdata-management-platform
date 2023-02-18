package com.bmp.search.core.provider.filter;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import com.bmp.commons.enums.SubjectType;
import com.bmp.dao.entity.Asset;
import com.bmp.dao.entity.Column;
import com.bmp.dao.entity.Datasource;
import com.bmp.dao.utils.BaseEntity;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.Collections;

public class SubjectFilter implements Filter {
    private final boolean disable;
    private final Collection<Integer> collectionIDs;

    private final Collection<Integer> datasourceIDs;

    private final Collection<Integer> assetIDs;

    public SubjectFilter(Collection<Integer> collectionIDs, Collection<Integer> datasourceIDs) {
        if (CollectionUtils.isEmpty(collectionIDs) && CollectionUtils.isEmpty(datasourceIDs)) {
            this.disable = true;
            this.collectionIDs = Collections.emptyList();
            this.datasourceIDs = Collections.emptyList();
            this.assetIDs = Collections.emptyList();
            return;
        }

        this.disable = false;

        if (CollectionUtils.isEmpty(datasourceIDs)) {
            this.collectionIDs = collectionIDs;

            this.datasourceIDs = SimpleQuery.list(
                    new LambdaQueryWrapper<Datasource>()
                            .select(Datasource::getId)
                            .in(Datasource::getCollectionID, this.collectionIDs),
                    Datasource::getId
            );
            if (CollectionUtils.isEmpty(this.datasourceIDs)) {
                this.assetIDs = Collections.emptyList();
                return;
            }
        }
        // collection is empty
        else {
            this.collectionIDs = Collections.emptyList();
            this.datasourceIDs = datasourceIDs;
        }

        // datasource is not empty
        this.assetIDs = SimpleQuery.list(
                new LambdaQueryWrapper<Asset>()
                        .select(Asset::getId)
                        .in(Asset::getDatasourceID, this.datasourceIDs),
                Asset::getId
        );
    }

    @Override
    @SuppressWarnings("deprecation")
    public <T extends BaseEntity> boolean pushdownToQuery(QueryWrapper<T> query, SubjectType type) {
        if (disable) {
            return true;
        }
        Collection<Integer> target;
        SFunction<T, ?> column = BaseEntity::getId;
        switch (type) {
            case COLLECTION:
                target = collectionIDs;
                break;
            case DATASOURCE:
                target = datasourceIDs;
                break;
            case ASSET:
            case DATABASE:
            case TABLE:
            case FILESET:
                target = assetIDs;
                break;
            case COLUMN:
                target = assetIDs;
                column = t -> ((Column) t).getAssetID();
                break;
            default:
                throw new IllegalArgumentException("unknown subject type");
        }
        if (CollectionUtils.isEmpty(target)) {
            query.apply("1 = 0");
        } else {
            query.lambda().in(column, target);
        }
        return true;
    }
}
