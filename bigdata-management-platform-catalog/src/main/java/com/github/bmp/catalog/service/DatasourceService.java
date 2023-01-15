package com.github.bmp.catalog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.bmp.catalog.dto.ListDatasourceRequest;
import com.github.bmp.catalog.vo.DatasourceView;
import com.github.bmp.commons.result.Result;
import com.github.bmp.dao.entity.Datasource;
import com.github.bmp.dao.utils.BaseService;

public interface DatasourceService extends BaseService<Datasource> {
    Result<Datasource> createDatasource(Datasource datasource);

    Result<Datasource> updateDatasource(Datasource datasource);

    IPage<DatasourceView> listDatasource(ListDatasourceRequest request);

    Result<?> deleteDatasource(int id);
}
