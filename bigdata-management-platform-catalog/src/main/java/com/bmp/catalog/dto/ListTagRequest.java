package com.bmp.catalog.dto;

import com.bmp.dao.utils.Paginator;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ListTagRequest implements ListRequest {
    private Paginator paginator;
    private List<Integer> ids;
    private List<SubjectID> subjectIDs;
}
