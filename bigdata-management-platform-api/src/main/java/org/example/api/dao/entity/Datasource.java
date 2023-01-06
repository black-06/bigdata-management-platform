package org.example.api.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("catalog_datasource")
public class Datasource {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    @TableField("create_time")
    private Date createTime;
    @TableField("update_time")
    private Date updateTime;

    @TableField("name")
    private String name;
    @TableField("description")
    private String description;
    @TableField("type")
    private DatasourceType type;
    @TableField("collection_id")
    private int collectionID;

}
