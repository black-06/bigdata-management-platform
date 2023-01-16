package com.bmp.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmp.commons.enums.SubjectType;
import com.bmp.dao.utils.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("catalog_column")
public class Column extends BaseEntity implements Subject {
    @TableField("name")
    private String name;
    @TableField("description")
    private String description;
    @TableField("asset_id")
    private Integer assetID;
    @TableField("type")
    private String type;
    @TableField("comment")
    private String comment;
    @TableField("default_value")
    private String defaultValue;

    @Override
    public SubjectType type() {
        return SubjectType.COLUMN;
    }

    @Override
    public Column setId(Integer id) {
        super.setId(id);
        return this;
    }

    @Override
    public Column setCreateTime(Instant createTime) {
        super.setCreateTime(createTime);
        return this;
    }

    @Override
    public Column setUpdateTime(Instant updateTime) {
        super.setUpdateTime(updateTime);
        return this;
    }
}
