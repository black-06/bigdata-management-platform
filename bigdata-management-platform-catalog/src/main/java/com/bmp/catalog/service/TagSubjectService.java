package com.bmp.catalog.service;

import com.bmp.catalog.dto.SubjectID;
import com.bmp.catalog.dto.UpdateTagSubjectRequest;
import com.bmp.catalog.vo.TagView;
import com.bmp.commons.result.Result;
import com.bmp.dao.entity.TagSubject;
import com.bmp.dao.utils.BaseService;

import java.util.List;
import java.util.Map;

public interface TagSubjectService extends BaseService<TagSubject> {

    Map<SubjectID, List<TagView>> listTagSubject(List<SubjectID> subjectIDs);

    Result<?> updateTagSubject(UpdateTagSubjectRequest request);
}
