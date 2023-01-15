package com.github.bmp.catalog.service;

import com.github.bmp.catalog.dto.SubjectID;
import com.github.bmp.catalog.dto.UpdateTagSubjectRequest;
import com.github.bmp.commons.result.Result;
import com.github.bmp.dao.entity.Tag;
import com.github.bmp.dao.entity.TagSubject;
import com.github.bmp.dao.utils.BaseService;

import java.util.List;
import java.util.Map;

public interface TagSubjectService extends BaseService<TagSubject> {

    Map<SubjectID, List<Tag>> listTagSubject(List<SubjectID> subjectIDs);

    Result<?> updateTagSubject(UpdateTagSubjectRequest request);
}
