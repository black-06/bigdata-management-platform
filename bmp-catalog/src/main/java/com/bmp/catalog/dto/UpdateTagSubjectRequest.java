package com.bmp.catalog.dto;

import com.bmp.dao.entity.TagSubject;
import lombok.Data;

import java.util.List;

@Data
public class UpdateTagSubjectRequest {
    private List<TagSubject> bind;
    private List<TagSubject> detach;
}
