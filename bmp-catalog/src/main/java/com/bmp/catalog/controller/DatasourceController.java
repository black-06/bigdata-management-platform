package com.bmp.catalog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bmp.catalog.dto.ListDatasourceRequest;
import com.bmp.catalog.service.DatasourceService;
import com.bmp.catalog.vo.DatasourceView;
import com.bmp.commons.exceptions.ApiException;
import com.bmp.commons.result.Result;
import com.bmp.commons.result.Status;
import com.bmp.dao.entity.Datasource;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("datasource")
@RequiredArgsConstructor
public class DatasourceController {

    private final DatasourceService datasourceService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @ApiException(value = Status.CREATE_ERROR_ARGS, args = {"datasource"})
    @JsonSerialize()
    public Result<?> createDatasource(@RequestBody Datasource datasource) {
        return datasourceService.createDatasource(datasource);
    }


    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiException(value = Status.UPDATE_ERROR_ARGS, args = {"datasource"})
    public Result<Datasource> updateDatasource(
            @PathVariable("id") int id, @RequestBody Datasource datasource
    ) {
        datasource.setId(id);
        return datasourceService.updateDatasource(datasource);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiException(value = Status.QUERY_ERROR_ARGS, args = {"datasource"})
    public Result<Datasource> queryDatasource(@PathVariable("id") int id) {
        return Result.success(datasourceService.getById(id));
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @ApiException(value = Status.QUERY_ERROR_ARGS, args = {"datasource"})
    public Result<IPage<DatasourceView>> queryDatasourceList(ListDatasourceRequest request) {
        return Result.success(datasourceService.listDatasource(request));
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiException(value = Status.DELETE_ERROR_ARGS, args = {"datasource"})
    public Result<?> deleteDatasource(@PathVariable("id") int id) {
        return datasourceService.deleteDatasource(id);
    }
}
