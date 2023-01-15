package com.github.bmp.dao.entity;

import com.github.bmp.commons.enums.SubjectType;

public interface Subject {

    SubjectType type();

    Integer getId();

    String getName();

    String getDescription();

}
