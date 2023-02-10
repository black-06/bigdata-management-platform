package com.bmp.catalog.vo;

import com.bmp.dao.entity.Asset;
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
    private List<Object[]> sampleData;

    public AssetView setTags(List<TagView> tags) {
        super.setTags(tags);
        return this;
    }
}
