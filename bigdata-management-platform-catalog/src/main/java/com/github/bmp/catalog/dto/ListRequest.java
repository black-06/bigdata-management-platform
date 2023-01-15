package com.github.bmp.catalog.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.bmp.dao.utils.Paginator;
import org.apache.commons.collections4.CollectionUtils;

public interface ListRequest {
    Paginator getPaginator();

    @JsonIgnore
    default <T> Page<T> getPage() {
        Paginator paginator = getPaginator();
        if (paginator == null) {
            return new Page<>(1, -1);
        }
        Page<T> page = new Page<>(paginator.getNum(), paginator.getSize());
        if (CollectionUtils.isNotEmpty(paginator.getOrders())) {
            page.addOrder(paginator.getOrders());
        }
        return page;
    }
}
