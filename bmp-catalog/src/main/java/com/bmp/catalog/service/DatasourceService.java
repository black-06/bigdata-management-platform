package com.bmp.catalog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bmp.catalog.dto.ListDatasourceRequest;
import com.bmp.catalog.vo.DatasourceView;
import com.bmp.commons.result.Result;
import com.bmp.dao.entity.Datasource;
import com.bmp.dao.utils.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DatasourceService extends BaseService<Datasource> {
    Result<Datasource> createDatasource(Datasource datasource);

    Result<Datasource> updateDatasource(Datasource datasource);

    IPage<DatasourceView> listDatasource(ListDatasourceRequest request);

    @Transactional
    Result<?> deleteDatasource(int id);

    @Transactional
    void batchDeleteDatasource(List<Integer> CollectionIDs);
}
