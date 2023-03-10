package com.bmp.catalog.metasync;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmp.catalog.service.AssetService;
import com.bmp.catalog.service.ColumnService;
import com.bmp.commons.enums.FileType;
import com.bmp.connector.api.list.AssetPath;
import com.bmp.connector.api.list.Lister;
import com.bmp.cron.Task;
import com.bmp.dao.entity.Asset;
import com.bmp.dao.entity.Column;
import com.bmp.dao.entity.Datasource;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public class MetaSyncTask extends Task {
    private static final Logger logger = LoggerFactory.getLogger(MetaSyncTask.class);
    @JsonInclude
    private Datasource datasource;
    private AssetService assetService;
    private ColumnService columnService;
    private BiFunction<AssetPath, Boolean, String> pathBuilder;

    @Override
    public String type() {
        return "meta_sync";
    }

    @Override
    public void run() throws Exception {
        List<Lister.Asset> assets = new ArrayList<>();
        try (Lister lister = datasource.getConnectorInfo().buildConnector().getLister()) {
            pathBuilder = lister::buildPath;
            for (AssetPath path : datasource.getSyncPaths()) {
                logger.debug("sync path: {}", path.getFullName("."));
                assets.addAll(lister.list(path));
            }
        }
        assets.forEach(this::saveAsset);
    }


    private Asset saveAsset(Lister.Asset asset) {
        Asset entity = convertAsset(asset);
        if (asset.getParent() != null) {
            Asset parent = saveAsset(asset.getParent());
            entity.setParentID(parent.getId());
        }
        if (asset.getColumns() == null) {
            return saveAssetIfNotFound(entity);
        } else {
            return saveLeafAsset(entity, asset.getColumns());
        }
    }

    @Transactional
    public Asset saveAssetIfNotFound(Asset asset) {
        List<Asset> assets = assetService.list(new QueryWrapper<Asset>()
                .lambda()
                .eq(Asset::getDatasourceID, asset.getDatasourceID())
                .eq(Asset::getParentID, asset.getParentID())
                .eq(Asset::getName, asset.getName())
                .eq(Asset::getType, asset.getType())
                .last("limit 1")
        );
        if (CollectionUtils.isNotEmpty(assets)) {
            return assets.get(0);
        }
        assetService.save(asset);
        return asset;
    }

    @Transactional
    public Asset saveLeafAsset(Asset asset, List<Lister.Column> columns) {
        asset = saveAssetIfNotFound(asset);
        columnService.saveBatch(convertColumns(asset.getId(), columns));
        return asset;
    }

    private Asset convertAsset(Lister.Asset asset) {
        Instant now = Instant.now();
        return new Asset()
                .setCreateTime(now)
                .setUpdateTime(now)
                .setName(asset.getName())
                .setDescription(null)
                .setParentID(0)
                .setDatasourceID(datasource.getId())
                .setType(asset.getType())
                .setPath(pathBuilder.apply(asset.getPath(), false))
                .setAssetPath(asset.getPath())
                .setFileType(asset.getFileType() == null ? FileType.NO : asset.getFileType())
                .setComment(null)
                .setDetails(null);
    }

    private List<Column> convertColumns(Integer assetID, List<Lister.Column> columns) {
        Instant now = Instant.now();
        return columns.stream().map(column -> new Column()
                .setCreateTime(now)
                .setUpdateTime(now)
                .setName(column.getField())
                .setDescription(null)
                .setAssetID(assetID)
                .setType(column.getType())
                .setComment(column.getComment())
                .setDefaultValue(column.getDefault())
        ).collect(Collectors.toList());
    }

    public static MetaSyncTask build(Datasource datasource, AssetService assetService, ColumnService columnService) {
        MetaSyncTask task = new MetaSyncTask();
        task.setDatasource(datasource);
        task.setAssetService(assetService);
        task.setColumnService(columnService);

        // calc execute delay
        Instant now = Instant.now();
        Instant executeTime = datasource.getSyncExecuteTime();
        Duration delay;
        if (executeTime == null || executeTime.isBefore(now)) {
            delay = Duration.ZERO;
        } else {
            delay = Duration.between(now, executeTime);
        }
        task.setExecuteDelay(delay);

        // calc recurring interval
        if (datasource.getSyncInterval() != null) {
            task.setRecurringInterval(datasource.getSyncInterval());
        }

        return task;
    }
}
