package com.github.bmp.search.core;

import com.github.bmp.commons.enums.SubjectType;
import com.github.bmp.dao.entity.Subject;
import com.github.bmp.dao.entity.Tag;
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
