package com.bmp.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.bmp.commons.enums.AssetType;
import com.bmp.commons.enums.FileType;
import com.bmp.commons.enums.SubjectType;
import com.bmp.connector.api.list.AssetPath;
import com.bmp.dao.utils.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.Instant;

/**
 * Asset stores database and table object.
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("catalog_asset")
public class Asset extends BaseEntity implements Subject {
    @TableField("name")
    private String name;
    @TableField("description")
    private String description;
    /**
     * If the asset is table, the parent id is database id.
     * If the asset is database, the parent id is 0 (no parent).
     */
    @TableField("parent_id")
    private Integer parentID;
    @TableField("datasource_id")
    private Integer datasourceID;
    @TableField("type")
    private AssetType type;
    @TableField("path")
    private String path;
    @JsonIgnore
    @TableField(value = "asset_path", typeHandler = JacksonTypeHandler.class)
    private AssetPath assetPath;
    /**
     * only for {@link AssetType#FILESET}
     */
    @TableField("file_type")
    private FileType fileType;
    @TableField("comment")
    private String comment;
    @TableField("details")
    private String details;

    @Override
    @SuppressWarnings("deprecation")
    public SubjectType type() {
        switch (type) {
            case DATABASE:
                return SubjectType.DATABASE;
            case TABLE:
                return SubjectType.TABLE;
            case FILESET:
                return SubjectType.FILESET;
            default:
                return SubjectType.ASSET;
        }
    }

    @Override
    public Asset setId(Integer id) {
        super.setId(id);
        return this;
    }

    @Override
    public Asset setCreateTime(Instant createTime) {
        super.setCreateTime(createTime);
        return this;
    }

    @Override
    public Asset setUpdateTime(Instant updateTime) {
        super.setUpdateTime(updateTime);
        return this;
    }
}
