package com.bmp.catalog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmp.catalog.dto.ListAssetRequest;
import com.bmp.catalog.dto.ListColumnRequest;
import com.bmp.catalog.dto.SubjectID;
import com.bmp.catalog.service.AssetService;
import com.bmp.catalog.service.ColumnService;
import com.bmp.catalog.service.TagSubjectService;
import com.bmp.catalog.vo.AssetView;
import com.bmp.catalog.vo.ColumnView;
import com.bmp.catalog.vo.TagView;
import com.bmp.commons.Box;
import com.bmp.commons.result.Result;
import com.bmp.commons.result.Status;
import com.bmp.dao.entity.Asset;
import com.bmp.dao.mapper.AssetMapper;
import com.bmp.dao.utils.BaseEntity;
import com.bmp.dao.utils.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl extends BaseServiceImpl<AssetMapper, Asset> implements AssetService {
    private final AssetMapper assetMapper;
    private final ColumnService columnService;
    private final TagSubjectService subjectService;

    @Override
    public IPage<AssetView> listAsset(ListAssetRequest request) {
        LambdaQueryWrapper<Asset> query = new LambdaQueryWrapper<>();
        if (CollectionUtils.isNotEmpty(request.getIds())) {
            query = query.in(Asset::getId, request.getIds());
        }
        if (Box.unbox(request.getParentID()) > 0) {
            query = query.eq(Asset::getParentID, request.getParentID());
        }
        if (Box.unbox(request.getDatasourceID()) > 0) {
            query = query.eq(Asset::getDatasourceID, request.getDatasourceID());
        }
        if (request.getType() != null) {
            query = query.eq(Asset::getType, request.getType());
        }
        Page<Asset> page = assetMapper.selectPage(request.getPage(), query);

        Map<SubjectID, List<TagView>> tags;
        if (Box.unbox(request.getWithTags())) {
            tags = subjectService.listTagSubject(SubjectID.ofList(page.getRecords()));
        } else {
            tags = Collections.emptyMap();
        }

        Map<Integer, List<ColumnView>> columns;
        if (Box.unbox(request.getWithColumns())) {
            columns = getColumns(page.getRecords(), request.getWithColumnTags());
        } else {
            columns = Collections.emptyMap();
        }

        return page.convert(asset -> new AssetView()
                .setAsset(asset)
                .setTags(tags.get(SubjectID.of(asset)))
                .setColumns(columns.get(asset.getId()))
        );
    }

    private Map<Integer, List<ColumnView>> getColumns(List<Asset> assets, Boolean withColumnTags) {
        if (CollectionUtils.isEmpty(assets)) {
            return Collections.emptyMap();
        }

        ListColumnRequest request = new ListColumnRequest();
        request.setAssetIDs(assets.stream().map(BaseEntity::getId).collect(Collectors.toList()));
        request.setWithTags(withColumnTags);

        return columnService
                .listColumn(request)
                .getRecords()
                .stream()
                .collect(Collectors.groupingBy(column -> column.getColumn().getAssetID()));
    }

    @Override
    public Result<Asset> updateAsset(Asset update) {
        Integer id = update.getId();
        if (id == null || id == 0) {
            return Result.error(Status.INVALID_PARAM_ARGS, "id");
        }
        Asset asset = assetMapper.selectById(id);
        if (asset == null) {
            return Result.error(Status.RESOURCE_NOTFOUND_ARGS, "asset id=" + id);
        }

        asset.setUpdateTime(Instant.now());
        if (StringUtils.isNotBlank(update.getDescription())) {
            asset.setDescription(update.getDescription());
        }
        assetMapper.updateById(asset);
        return Result.success(asset);
    }

    @Override
    @Transactional
    public void batchDeleteAsset(List<Integer> datasourceIDs) {
        // query subject id
        List<SubjectID> subjectList = new ArrayList<>();
        List<Integer> assetIDs = new ArrayList<>();
        LambdaQueryWrapper<Asset> query = new LambdaQueryWrapper<Asset>().in(Asset::getDatasourceID, datasourceIDs);
        for (Asset asset : assetMapper.selectList(query)) {
            subjectList.add(SubjectID.of(asset));
            assetIDs.add(asset.getId());
        }
        subjectService.detachSubject(subjectList);
        assetMapper.delete(query);
        columnService.batchDeleteColumns(assetIDs);
    }
}
