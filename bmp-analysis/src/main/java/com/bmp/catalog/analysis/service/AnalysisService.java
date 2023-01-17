package com.bmp.catalog.analysis.service;

import com.bmp.catalog.analysis.dto.SubjectAnalysis;
import com.bmp.catalog.analysis.dto.TagAnalysis;
import com.bmp.commons.result.Result;
import com.bmp.dao.entity.Asset;

/**
 * AnalysisService is real-time, It counts other tables such as {@link Asset}.
 */
public interface AnalysisService {
    Result<SubjectAnalysis> getSubjectAnalysis();

    Result<TagAnalysis> getTagAnalysis();
}
