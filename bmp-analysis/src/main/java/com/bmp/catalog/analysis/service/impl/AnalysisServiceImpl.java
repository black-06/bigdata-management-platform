package com.bmp.catalog.analysis.service.impl;

import com.bmp.catalog.analysis.dto.SubjectAnalysis;
import com.bmp.catalog.analysis.dto.TagAnalysis;
import com.bmp.catalog.analysis.service.AnalysisService;
import com.bmp.commons.result.Result;
import com.bmp.commons.result.Status;
import org.springframework.stereotype.Service;

@Service
public class AnalysisServiceImpl implements AnalysisService {
    @Override
    public Result<SubjectAnalysis> getSubjectAnalysis() {
        return Result.error(Status.INTERNAL_SERVER_ERROR_ARGS, "not implemented");
    }

    @Override
    public Result<TagAnalysis> getTagAnalysis() {
        return Result.error(Status.INTERNAL_SERVER_ERROR_ARGS, "not implemented");
    }
}
