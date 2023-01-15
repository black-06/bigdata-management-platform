package com.github.bmp.catalog.vo;

import com.github.bmp.dao.entity.Datasource;
import com.github.bmp.dao.entity.Tag;
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

    public DatasourceView setTags(List<Tag> tags) {
        super.setTags(tags);
        return this;
    }
}
