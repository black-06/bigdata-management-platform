package org.example.api.service;

import org.example.api.dao.entity.Collection;
import org.example.api.utils.result.Result;

import java.util.List;

public interface CollectionService {
    Result<?> createCollection(Collection collection);

    Result<List<Collection>> listCollection();
}
