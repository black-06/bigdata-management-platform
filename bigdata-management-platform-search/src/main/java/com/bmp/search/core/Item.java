package com.bmp.search.core;

import com.bmp.commons.enums.SubjectType;
import com.bmp.dao.entity.Subject;
import com.bmp.dao.entity.Tag;
import lombok.Data;

import java.util.List;

@Data
public class Item {
    private int id;
    private String name;
    private String description;
    private SubjectType type;
    private Subject subject;
    private List<Tag> tags;
}
