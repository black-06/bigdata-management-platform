package com.bmp.catalog.controller;

import com.bmp.catalog.service.CollectionService;
import com.bmp.commons.exceptions.ApiException;
import com.bmp.commons.result.Result;
import com.bmp.commons.result.Status;
import com.bmp.dao.entity.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("collection")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @ApiException(value = Status.CREATE_ERROR_ARGS, args = {"collection"})
    public Result<?> createCollection(@RequestBody Collection collection) {
        return Result.success(collectionService.insert(collection));
    }


    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiException(value = Status.UPDATE_ERROR_ARGS, args = {"collection"})
    public Result<Collection> updateCollection(
            @PathVariable("id") int id, @RequestBody Collection collection
    ) {
        collection.setId(id);
        return collectionService.updateCollection(collection);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiException(value = Status.QUERY_ERROR_ARGS, args = {"collection"})
    public Result<Collection> queryCollection(@PathVariable("id") int id) {
        return Result.success(collectionService.getById(id));
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @ApiException(value = Status.QUERY_ERROR_ARGS, args = {"collection"})
    public Result<List<Collection>> queryCollectionList() {
        return collectionService.listCollection();
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiException(value = Status.DELETE_ERROR_ARGS, args = {"collection"})
    public Result<?> deleteCollection(
            @PathVariable("id") int id, @RequestParam("delete_datasource") boolean deleteDatasource
    ) {
        return collectionService.deleteCollection(id, deleteDatasource);
    }
}
