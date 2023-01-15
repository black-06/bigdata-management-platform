package com.github.bmp.catalog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.bmp.catalog.dto.ListTagRequest;
import com.github.bmp.commons.result.Result;
import com.github.bmp.dao.entity.Tag;
import com.github.bmp.dao.utils.BaseService;

public interface TagService extends BaseService<Tag> {
    Result<IPage<Tag>> listTag(ListTagRequest request);

    Result<Tag> updateTag(Tag tag);

    Result<?> deleteTag(int id);
}
