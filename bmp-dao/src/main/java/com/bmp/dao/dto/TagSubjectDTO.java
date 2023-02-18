package com.bmp.dao.dto;

import com.bmp.commons.enums.SubjectType;
import com.bmp.dao.entity.Tag;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TagSubjectDTO extends Tag {
    @JsonIgnore
    private Integer subjectID;
    @JsonIgnore
    private SubjectType subjectType;
}