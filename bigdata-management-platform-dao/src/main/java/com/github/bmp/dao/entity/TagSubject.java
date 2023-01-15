package com.github.bmp.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.bmp.commons.enums.SubjectType;
import com.github.bmp.dao.utils.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("catalog_relation_tag_subject")
public class TagSubject extends BaseEntity {
    @TableField("tag_id")
    private Integer tagID;
    @TableField("subject_id")
    private Integer subjectID;
    @TableField("subject_type")
    private SubjectType subjectType;

    @Override
    public TagSubject setId(Integer id) {
        super.setId(id);
        return this;
    }

    @Override
    public TagSubject setCreateTime(Instant createTime) {
        super.setCreateTime(createTime);
        return this;
    }

    @Override
    public TagSubject setUpdateTime(Instant updateTime) {
        super.setUpdateTime(updateTime);
        return this;
    }
}
