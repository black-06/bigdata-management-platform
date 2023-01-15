package com.github.bmp.catalog.analysis.service;

import com.github.bmp.catalog.analysis.dto.SubjectAnalysis;
import com.github.bmp.catalog.analysis.dto.TagAnalysis;
import com.github.bmp.commons.result.Result;
import com.github.bmp.dao.entity.Asset;

/**
 * AnalysisService is real-time, It counts other tables such as {@link Asset}.
 */
public interface AnalysisService {
    Result<SubjectAnalysis> getSubjectAnalysis();

    Result<TagAnalysis> getTagAnalysis();
}
