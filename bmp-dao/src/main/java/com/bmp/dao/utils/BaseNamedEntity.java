package com.bmp.dao.utils;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class BaseNamedEntity extends BaseEntity {
    @TableField(value = "name", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String name;
    @TableField(value = "description", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String description;

    @JsonIgnore
    public String GetFullTextKey() {
        return "`name`, `description`";
    }
}
