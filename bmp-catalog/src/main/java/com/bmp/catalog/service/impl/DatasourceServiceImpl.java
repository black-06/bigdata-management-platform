package com.bmp.catalog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmp.catalog.dto.ListAssetRequest;
import com.bmp.catalog.dto.ListDatasourceRequest;
import com.bmp.catalog.dto.SubjectID;
import com.bmp.catalog.service.AssetService;
import com.bmp.catalog.service.DatasourceService;
import com.bmp.catalog.service.TagSubjectService;
import com.bmp.catalog.vo.AssetView;
import com.bmp.catalog.vo.DatasourceView;
import com.bmp.catalog.vo.TagView;
import com.bmp.commons.Box;
import com.bmp.commons.enums.SubjectType;
import com.bmp.commons.result.Result;
import com.bmp.commons.result.Status;
import com.bmp.connector.api.ConnectorInfo;
import com.bmp.dao.entity.Datasource;
import com.bmp.dao.mapper.DatasourceMapper;
import com.bmp.dao.utils.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.bmp.cron.Task.MINIMAL_INTERVAL;

@Service
@RequiredArgsConstructor
public class DatasourceServiceImpl extends BaseServiceImpl<DatasourceMapper, Datasource> implements DatasourceService {
    private final DatasourceMapper datasourceMapper;
    private final AssetService assetService;
    private final TagSubjectService subjectService;

    @Override
    public Result<Datasource> createDatasource(Datasource datasource) {
        Instant now = Instant.now();
        datasource.setId(null);
        datasource.setCreateTime(now);
        datasource.setUpdateTime(now);
        if (datasource.getStatus() == null) {
            datasource.setStatus(com.bmp.commons.enums.Status.ACTIVE);
        }
        if (datasource.getType() == null) {
            return Result.error(Status.INVALID_PARAM_ARGS, "unknown datasource type");
        }
        if (datasource.getConnectorInfo() == null) {
            return Result.error(Status.INVALID_PARAM_ARGS, "connector info should not be null");
        }
        try {
            datasource.getConnectorInfo().buildConnector().ping();
        } catch (Exception e) {
            return Result.error(Status.CONNECTOR_PING_ERROR_ARGS, e.getMessage());
        }
        Duration interval = datasource.getSyncInterval();
        if (interval != null && interval.compareTo(MINIMAL_INTERVAL) < 0) {
            return Result.error(Status.INVALID_PARAM_ARGS, "sync interval should be greater than 1s");
        }
        datasourceMapper.insert(datasource);
        return Result.success(datasource);
    }

    @Override
    public IPage<DatasourceView> listDatasource(ListDatasourceRequest request) {
        LambdaQueryWrapper<Datasource> query = new LambdaQueryWrapper<>();
        if (CollectionUtils.isNotEmpty(request.getIds())) {
            query = query.in(Datasource::getId, request.getIds());
        }
        if (Box.unbox(request.getCollectionID()) > 0) {
            query = query.eq(Datasource::getCollectionID, request.getCollectionID());
        }
        if (request.getType() != null) {
            query = query.eq(Datasource::getType, request.getType());
        }

        Page<Datasource> page = datasourceMapper.selectPage(request.getPage(), query);

        Map<SubjectID, List<TagView>> tags;
        if (Box.unbox(request.getWithTags())) {
            tags = subjectService.listTagSubject(SubjectID.ofList(page.getRecords()));
        } else {
            tags = Collections.emptyMap();
        }

        Map<Integer, List<AssetView>> assets;
        if (Box.unbox(request.getWithAssets())) {
            assets = getAssets(
                    page.getRecords(),
                    request.getWithAssetTags(),
                    request.getWithAssetColumns(),
                    request.getWithAssetColumnTags()
            );
        } else {
            assets = Collections.emptyMap();
        }

        return page.convert(datasource -> new DatasourceView()
                .setDatasource(datasource)
                .setTags(tags.get(SubjectID.of(datasource)))
                .setAssets(assets.get(datasource.getId()))
        );
    }

    private Map<Integer, List<AssetView>> getAssets(
            List<Datasource> datasources,
            Boolean withAssetTags,
            Boolean withAssetColumns,
            Boolean withAssetColumnTags
    ) {
        if (CollectionUtils.isEmpty(datasources)) {
            return Collections.emptyMap();
        }

        ListAssetRequest request = new ListAssetRequest();
        request.setDatasourceIDs(datasources.stream().map(Datasource::getId).collect(Collectors.toList()));
        request.setWithTags(withAssetTags);
        request.setWithColumns(withAssetColumns);
        request.setWithColumnTags(withAssetColumnTags);

        return assetService
                .listAsset(request)
                .getRecords()
                .stream()
                .collect(Collectors.groupingBy(asset -> asset.getAsset().getDatasourceID()));
    }

    @Override
    public Result<Datasource> updateDatasource(Datasource update) {
        Integer id = update.getId();
        if (id == null || id == 0) {
            return Result.error(Status.INVALID_PARAM_ARGS, "id");
        }
        Datasource datasource = datasourceMapper.selectById(id);
        if (datasource == null) {
            return Result.error(Status.RESOURCE_NOTFOUND_ARGS, "datasource id=", id);
        }
        datasource.setUpdateTime(Instant.now());
        if (StringUtils.isNotBlank(update.getName())) {
            datasource.setName(update.getName());
        }
        if (StringUtils.isNotBlank(update.getDescription())) {
            datasource.setDescription(update.getDescription());
        }
        if (update.getStatus() != null) {
            datasource.setStatus(update.getStatus());
        }
        if (update.getType() != null) {
            datasource.setType(update.getType());
        }
        ConnectorInfo info = update.getConnectorInfo();
        if (info != null) {
            try {
                info.buildConnector().ping();
            } catch (SQLException e) {
                return Result.error(Status.CONNECTOR_PING_ERROR_ARGS, e.getMessage());
            }
            datasource.setConnectorInfo(info);
        }
        if (update.getCollectionID() != null) {
            datasource.setCollectionID(update.getCollectionID());
        }
        if (CollectionUtils.isNotEmpty(update.getSyncPaths())) {
            datasource.setSyncPaths(update.getSyncPaths());
        }
        Duration interval = datasource.getSyncInterval();
        if (interval != null) {
            if (interval.compareTo(MINIMAL_INTERVAL) < 0) {
                return Result.error(Status.INVALID_PARAM_ARGS, "sync interval should be greater than 1s");
            }
            datasource.setSyncInterval(interval);
        }
        datasourceMapper.updateById(datasource);
        return Result.success(datasource);
    }

    @Override
    @Transactional
    public Result<?> deleteDatasource(int id) {
        subjectService.detachSubject(Collections.singletonList(new SubjectID(id, SubjectType.DATASOURCE)));
        datasourceMapper.deleteById(id);
        assetService.batchDeleteAsset(Collections.singletonList(id));
        return Result.success(null);
    }

    @Override
    @Transactional
    public void batchDeleteDatasource(List<Integer> CollectionIDs) {
        // query subject id
        List<SubjectID> subjectList = new ArrayList<>();
        List<Integer> datasourceIDs = new ArrayList<>();
        LambdaQueryWrapper<Datasource> query = new LambdaQueryWrapper<Datasource>().in(Datasource::getCollectionID, CollectionIDs);
        for (Datasource datasource : datasourceMapper.selectList(query)) {
            subjectList.add(SubjectID.of(datasource));
            datasourceIDs.add(datasource.getId());
        }
        subjectService.detachSubject(subjectList);
        datasourceMapper.delete(query);
        assetService.batchDeleteAsset(datasourceIDs);
    }
}
