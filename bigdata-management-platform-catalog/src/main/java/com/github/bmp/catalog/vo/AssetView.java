package com.github.bmp.catalog.vo;

import com.github.bmp.dao.entity.Asset;
import com.github.bmp.dao.entity.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class AssetView extends BaseView {
    private Asset asset;
    private List<ColumnView> columns;

    public AssetView setTags(List<Tag> tags) {
        super.setTags(tags);
        return this;
    }
}
