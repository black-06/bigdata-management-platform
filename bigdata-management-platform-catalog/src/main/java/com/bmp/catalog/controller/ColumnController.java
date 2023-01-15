package com.bmp.catalog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bmp.catalog.dto.ListColumnRequest;
import com.bmp.catalog.service.ColumnService;
import com.bmp.catalog.vo.ColumnView;
import com.bmp.commons.exceptions.ApiException;
import com.bmp.commons.result.Result;
import com.bmp.commons.result.Status;
import com.bmp.dao.entity.Column;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("column")
@RequiredArgsConstructor
public class ColumnController {

    private final ColumnService columnService;


    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiException(value = Status.UPDATE_ERROR_ARGS, args = {"asset"})
    public Result<Column> updateColumn(
            @PathVariable("id") int id, @RequestBody Column column
    ) {
        column.setId(id);
        return columnService.updateColumn(column);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @ApiException(value = Status.QUERY_ERROR_ARGS, args = {"asset"})
    public Result<IPage<ColumnView>> queryColumnList(ListColumnRequest request) {
        return Result.success(columnService.listColumn(request));
    }

}
