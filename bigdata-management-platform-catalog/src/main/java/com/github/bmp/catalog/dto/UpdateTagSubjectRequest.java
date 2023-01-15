package com.github.bmp.catalog.dto;

import com.github.bmp.dao.entity.TagSubject;
import lombok.Data;

import java.util.List;

@Data
public class UpdateTagSubjectRequest {
    private List<TagSubject> bind;
    private List<TagSubject> detach;
}
