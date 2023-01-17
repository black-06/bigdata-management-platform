package com.bmp.catalog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bmp.catalog.dto.ListTagRequest;
import com.bmp.commons.result.Result;
import com.bmp.dao.entity.Tag;
import com.bmp.dao.utils.BaseService;

public interface TagService extends BaseService<Tag> {
    Result<IPage<Tag>> listTag(ListTagRequest request);

    Result<Tag> updateTag(Tag tag);

    Result<?> deleteTag(int id);
}
