package com.bmp.dao.utils;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Accessors(chain = true)
public class BaseEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("create_time")
    private Instant createTime;
    @TableField("update_time")
    private Instant updateTime;
}
