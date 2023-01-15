package com.bmp.catalog.vo;

import com.bmp.dao.entity.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ColumnView extends BaseView {
    private Column column;

    public ColumnView setTags(List<TagView> tags) {
        super.setTags(tags);
        return this;
    }
}
