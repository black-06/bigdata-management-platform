package com.bmp.search.core.packer;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.bmp.commons.enums.SubjectType;
import com.bmp.dao.entity.Collection;
import com.bmp.dao.entity.*;
import com.bmp.search.core.Item;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

public class SubjectPacker implements Packer {

    @Override
    @SuppressWarnings("deprecation")
    public List<Item> pack(List<Item> items) {
        final Map<Integer, Collection> existedCollections = new HashMap<>();
        final Map<Integer, Datasource> existedDatasources = new HashMap<>();
        final Map<Integer, Asset> existedAssets = new HashMap<>();
        final List<Integer> queryCollectionIDs = new ArrayList<>();
        final List<Integer> queryDatasourceIDs = new ArrayList<>();
        final List<Integer> queryAssetIDs = new ArrayList<>();

        items.forEach(item -> {
            Subject subject = item.getSubject();
            switch (item.getType()) {
                case COLLECTION:
                    existedCollections.put(subject.getId(), (Collection) subject);
                    return;
                case DATASOURCE:
                    Datasource datasource = (Datasource) subject;

                    Integer collectionID = datasource.getCollectionID();
                    if (!existedCollections.containsKey(collectionID)) {
                        queryCollectionIDs.add(collectionID);
                    }

                    existedDatasources.put(datasource.getId(), datasource);
                    return;
                case ASSET:
                case DATABASE:
                case TABLE:
                case FILESET:
                    Asset asset = (Asset) subject;
                    Integer datasourceID = asset.getDatasourceID();
                    if (!existedDatasources.containsKey(datasourceID)) {
                        queryDatasourceIDs.add(datasourceID);
                    }

                    Integer parentID = asset.getParentID();
                    if (parentID != null && parentID != 0 && !existedAssets.containsKey(parentID)) {
                        queryAssetIDs.add(parentID);
                    }

                    existedAssets.put(asset.getId(), asset);
                    return;
                case COLUMN:
                    Integer assetID = ((Column) subject).getAssetID();
                    if (!existedAssets.containsKey(assetID)) {
                        queryAssetIDs.add(assetID);
                    }
                    return;
                default:
                    throw new IllegalArgumentException("unknown subject type");
            }
        });

        // find asset
        if (CollectionUtils.isNotEmpty(queryAssetIDs)) {
            findAsset(existedDatasources, existedAssets, queryDatasourceIDs, queryAssetIDs);
        }
        // find datasource
        if (CollectionUtils.isNotEmpty(queryDatasourceIDs)) {
            Db.listByIds(queryDatasourceIDs, Datasource.class).forEach(datasource -> {
                Integer collectionID = datasource.getCollectionID();
                if (!existedCollections.containsKey(collectionID)) {
                    queryCollectionIDs.add(collectionID);
                }

                existedDatasources.put(datasource.getId(), datasource);
            });
        }
        // find collection
        if (CollectionUtils.isNotEmpty(queryCollectionIDs)) {
            Db.listByIds(queryCollectionIDs, Collection.class)
                    .forEach(collection -> existedCollections.put(collection.getId(), collection));
        }

        items.forEach(item -> {
            Subject subject = item.getSubject();
            switch (item.getType()) {
                case COLLECTION:
                    item.setCollection((Collection) subject);
                    return;
                case DATASOURCE:
                    Datasource datasource = (Datasource) subject;
                    item.setCollection(existedCollections.get(datasource.getCollectionID()));
                    item.setDatasource(datasource);
                    return;
                case ASSET:
                case DATABASE:
                case TABLE:
                case FILESET:
                case COLUMN:
                    Asset asset;
                    if (item.getType() == SubjectType.COLUMN) {
                        asset = existedAssets.get(((Column) subject).getAssetID());
                    } else {
                        asset = (Asset) subject;
                    }

                    datasource = existedDatasources.get(asset.getDatasourceID());
                    item.setDatasource(datasource);
                    item.setCollection(existedCollections.get(datasource.getCollectionID()));

                    List<Asset> assets = new ArrayList<>();
                    while (asset != null) {
                        assets.add(asset);
                        asset = existedAssets.get(asset.getParentID());
                    }
                    Collections.reverse(assets);
                    item.setAssets(assets);
                    return;
                default:
                    throw new IllegalArgumentException("unknown subject type");
            }
        });
        return items;
    }

    private void findAsset(
            final Map<Integer, Datasource> existedDatasources,
            final Map<Integer, Asset> existedAssets,
            final List<Integer> queryDatasourceIDs,
            final List<Integer> queryAssetIDs
    ) {
        List<Integer> parentIDs = new ArrayList<>();
        Db.listByIds(queryAssetIDs, Asset.class).forEach(asset -> {
            Integer datasourceID = asset.getDatasourceID();
            if (!existedDatasources.containsKey(datasourceID)) {
                queryDatasourceIDs.add(datasourceID);
            }

            Integer parentID = asset.getParentID();
            if (parentID != null && parentID != 0 && !existedAssets.containsKey(parentID)) {
                parentIDs.add(parentID);
            }

            existedAssets.put(asset.getId(), asset);
        });
        if (CollectionUtils.isNotEmpty(parentIDs)) {
            findAsset(existedDatasources, existedAssets, queryDatasourceIDs, parentIDs);
        }
    }
}
