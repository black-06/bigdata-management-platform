package com.bmp.dao.entity;

import com.bmp.commons.enums.SubjectType;

public interface Subject {

    SubjectType type();

    Integer getId();

    String getName();

    String getDescription();

}
