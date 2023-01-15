package com.github.bmp.catalog.vo;

import com.github.bmp.dao.entity.Tag;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class BaseView {
    private List<Tag> tags;
}
