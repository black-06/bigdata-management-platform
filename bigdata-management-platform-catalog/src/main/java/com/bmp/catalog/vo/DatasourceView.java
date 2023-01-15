package com.bmp.catalog.vo;

import com.bmp.dao.entity.Datasource;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class DatasourceView extends BaseView {
    private Datasource datasource;
    private List<AssetView> assets;

    public DatasourceView setTags(List<TagView> tags) {
        super.setTags(tags);
        return this;
    }
}
