package org.example.api.connector;

import lombok.RequiredArgsConstructor;
import org.example.api.dao.entity.Collection;
import org.example.api.service.CollectionService;
import org.example.api.utils.exceptions.ApiException;
import org.example.api.utils.result.Result;
import org.example.api.utils.result.Status;
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
    @ApiException(Status.CREATE_COLLECTION_ERROR)
    public Result<?> createCollection(@RequestBody Collection collection) {
        return collectionService.createCollection(collection);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @ApiException(Status.QUERY_COLLECTION_ERROR)
    public Result<List<Collection>> queryCollectionList() {
        return collectionService.listCollection();
    }
}
