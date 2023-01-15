package com.github.bmp.catalog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.bmp.catalog.dto.ListAssetRequest;
import com.github.bmp.catalog.dto.ListDatasourceRequest;
import com.github.bmp.catalog.dto.SubjectID;
import com.github.bmp.catalog.service.AssetService;
import com.github.bmp.catalog.service.DatasourceService;
import com.github.bmp.catalog.service.TagSubjectService;
import com.github.bmp.catalog.vo.AssetView;
import com.github.bmp.catalog.vo.DatasourceView;
import com.github.bmp.commons.Box;
import com.github.bmp.commons.result.Result;
import com.github.bmp.commons.result.Status;
import com.github.bmp.dao.entity.Datasource;
import com.github.bmp.dao.entity.Tag;
import com.github.bmp.dao.mapper.DatasourceMapper;
import com.github.bmp.dao.utils.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            datasource.setStatus(com.github.bmp.commons.enums.Status.ACTIVE);
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

        Map<SubjectID, List<Tag>> tags;
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
    public Result<Datasource> updateDatasource(Datasource datasource) {
        return Result.error(Status.INTERNAL_SERVER_ERROR_ARGS, "not implemented");
    }

    @Override
    public Result<?> deleteDatasource(int id) {
        return Result.error(Status.INTERNAL_SERVER_ERROR_ARGS, "not implemented");
    }
}
