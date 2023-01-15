package com.github.bmp.catalog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.bmp.catalog.dto.ListAssetRequest;
import com.github.bmp.catalog.dto.ListColumnRequest;
import com.github.bmp.catalog.dto.SubjectID;
import com.github.bmp.catalog.service.AssetService;
import com.github.bmp.catalog.service.ColumnService;
import com.github.bmp.catalog.service.TagSubjectService;
import com.github.bmp.catalog.vo.AssetView;
import com.github.bmp.catalog.vo.ColumnView;
import com.github.bmp.commons.Box;
import com.github.bmp.commons.result.Result;
import com.github.bmp.commons.result.Status;
import com.github.bmp.dao.entity.Asset;
import com.github.bmp.dao.entity.Tag;
import com.github.bmp.dao.mapper.AssetMapper;
import com.github.bmp.dao.utils.BaseEntity;
import com.github.bmp.dao.utils.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

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

        Map<SubjectID, List<Tag>> tags;
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
    public Result<Asset> updateAsset(Asset asset) {
        return Result.error(Status.INTERNAL_SERVER_ERROR_ARGS, "not implemented");
    }

    @Override
    public Result<?> deleteAsset(int id) {
        return Result.error(Status.INTERNAL_SERVER_ERROR_ARGS, "not implemented");
    }
}
