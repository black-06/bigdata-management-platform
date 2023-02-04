package com.bmp.catalog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bmp.catalog.dto.ListColumnRequest;
import com.bmp.catalog.vo.ColumnView;
import com.bmp.commons.result.Result;
import com.bmp.dao.entity.Column;
import com.bmp.dao.utils.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ColumnService extends BaseService<Column> {
    Result<Column> updateColumn(Column column);

    IPage<ColumnView> listColumn(ListColumnRequest request);

    @Transactional
    void batchDeleteColumns(List<Integer> assetIDs);
}
