package com.github.bmp.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.bmp.dao.utils.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("catalog_tag")
public class Tag extends BaseEntity {
    @TableField("name")
    private String name;
    @TableField("description")
    private String description;

    @Override
    public Tag setId(Integer id) {
        super.setId(id);
        return this;
    }

    @Override
    public Tag setCreateTime(Instant createTime) {
        super.setCreateTime(createTime);
        return this;
    }

    @Override
    public Tag setUpdateTime(Instant updateTime) {
        super.setUpdateTime(updateTime);
        return this;
    }
}
