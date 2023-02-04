package com.bmp.catalog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmp.catalog.dto.ListColumnRequest;
import com.bmp.catalog.dto.SubjectID;
import com.bmp.catalog.service.ColumnService;
import com.bmp.catalog.service.TagSubjectService;
import com.bmp.catalog.vo.ColumnView;
import com.bmp.catalog.vo.TagView;
import com.bmp.commons.Box;
import com.bmp.commons.result.Result;
import com.bmp.commons.result.Status;
import com.bmp.dao.entity.Column;
import com.bmp.dao.mapper.ColumnMapper;
import com.bmp.dao.utils.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ColumnServiceImpl extends BaseServiceImpl<ColumnMapper, Column> implements ColumnService {
    private final ColumnMapper columnMapper;
    private final TagSubjectService subjectService;

    @Override
    public IPage<ColumnView> listColumn(ListColumnRequest request) {
        LambdaQueryWrapper<Column> query = new LambdaQueryWrapper<>();
        if (CollectionUtils.isNotEmpty(request.getIds())) {
            query = query.in(Column::getId, request.getIds());
        }
        if (CollectionUtils.isNotEmpty(request.getAssetIDs())) {
            query = query.in(Column::getAssetID, request.getAssetIDs());
        }
        if (Box.unbox(request.getAssetID()) > 0) {
            query = query.eq(Column::getAssetID, request.getAssetID());
        }
        Page<Column> page = columnMapper.selectPage(request.getPage(), query);

        Map<SubjectID, List<TagView>> tags;
        if (Box.unbox(request.getWithTags())) {
            tags = subjectService.listTagSubject(SubjectID.ofList(page.getRecords()));
        } else {
            tags = Collections.emptyMap();
        }
        return page.convert(column -> new ColumnView()
                .setColumn(column)
                .setTags(tags.get(SubjectID.of(column)))
        );
    }


    @Override
    public Result<Column> updateColumn(Column update) {
        Integer id = update.getId();
        if (id == null || id == 0) {
            return Result.error(Status.INVALID_PARAM_ARGS, "id");
        }
        Column column = columnMapper.selectById(id);
        if (column == null) {
            return Result.error(Status.RESOURCE_NOTFOUND_ARGS, "column id=" + id);
        }

        column.setUpdateTime(Instant.now());
        if (StringUtils.isNotBlank(update.getDescription())) {
            column.setDescription(update.getDescription());
        }
        columnMapper.updateById(column);
        return Result.success(column);
    }

    @Override
    @Transactional
    public void batchDeleteColumns(List<Integer> assetIDs) {
        LambdaQueryWrapper<Column> query = new LambdaQueryWrapper<Column>().in(Column::getAssetID, assetIDs);
        subjectService.detachSubject(SubjectID.ofList(columnMapper.selectList(query)));
        columnMapper.delete(query);
    }
}
