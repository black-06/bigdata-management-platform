package com.github.bmp.catalog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.bmp.catalog.dto.ListColumnRequest;
import com.github.bmp.catalog.vo.ColumnView;
import com.github.bmp.commons.result.Result;
import com.github.bmp.dao.entity.Column;
import com.github.bmp.dao.utils.BaseService;

public interface ColumnService extends BaseService<Column> {
    Result<Column> updateColumn(Column column);

    IPage<ColumnView> listColumn(ListColumnRequest request);
}
