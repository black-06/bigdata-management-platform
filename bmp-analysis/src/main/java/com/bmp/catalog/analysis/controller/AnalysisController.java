package com.bmp.catalog.analysis.controller;

import com.bmp.catalog.analysis.dto.SubjectAnalysis;
import com.bmp.catalog.analysis.dto.TagAnalysis;
import com.bmp.catalog.analysis.service.AnalysisService;
import com.bmp.commons.exceptions.ApiException;
import com.bmp.commons.result.Result;
import com.bmp.commons.result.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("analysis")
@RequiredArgsConstructor
public class AnalysisController {

    private final AnalysisService analysisService;

    @GetMapping("/subject")
    @ResponseStatus(HttpStatus.OK)
    @ApiException(value = Status.QUERY_ERROR_ARGS, args = {"analysis"})
    public Result<SubjectAnalysis> querySubjectAnalysis() {
        return analysisService.getSubjectAnalysis();
    }

    @GetMapping("/tag")
    @ResponseStatus(HttpStatus.OK)
    @ApiException(value = Status.QUERY_ERROR_ARGS, args = {"analysis"})
    public Result<TagAnalysis> queryTagAnalysis() {
        return analysisService.getTagAnalysis();
    }
}
