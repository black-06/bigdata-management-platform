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
@TableName("catalog_collection")
public class Collection extends BaseEntity implements Subject {
    @TableField("name")
    private String name;
    @TableField("description")
    private String description;

    @Override
    public SubjectType type() {
        return SubjectType.COLLECTION;
    }

    @Override
    public Collection setId(Integer id) {
        super.setId(id);
        return this;
    }

    @Override
    public Collection setCreateTime(Instant createTime) {
        super.setCreateTime(createTime);
        return this;
    }

    @Override
    public Collection setUpdateTime(Instant updateTime) {
        super.setUpdateTime(updateTime);
        return this;
    }
}
