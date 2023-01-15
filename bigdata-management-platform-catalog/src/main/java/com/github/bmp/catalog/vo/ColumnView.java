package com.github.bmp.catalog.vo;

import com.github.bmp.dao.entity.Column;
import com.github.bmp.dao.entity.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ColumnView extends BaseView {
    private Column column;

    public ColumnView setTags(List<Tag> tags) {
        super.setTags(tags);
        return this;
    }
}
