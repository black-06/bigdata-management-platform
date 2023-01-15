package com.bmp.catalog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bmp.catalog.dto.ListTagRequest;
import com.bmp.catalog.dto.UpdateTagSubjectRequest;
import com.bmp.catalog.service.TagService;
import com.bmp.catalog.service.TagSubjectService;
import com.bmp.commons.exceptions.ApiException;
import com.bmp.commons.result.Result;
import com.bmp.commons.result.Status;
import com.bmp.dao.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final TagSubjectService subjectService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @ApiException(value = Status.CREATE_ERROR_ARGS, args = {"tag"})
    public Result<?> createTag(@RequestBody List<Tag> tags) {
        return Result.success(tagService.insertBatch(tags));
    }


    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiException(value = Status.UPDATE_ERROR_ARGS, args = {"tag"})
    public Result<Tag> updateTag(
            @PathVariable("id") int id, @RequestBody Tag tag
    ) {
        tag.setId(id);
        return tagService.updateTag(tag);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiException(value = Status.QUERY_ERROR_ARGS, args = {"tag"})
    public Result<Tag> queryTag(@PathVariable("id") int id) {
        return Result.success(tagService.getById(id));
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @ApiException(value = Status.QUERY_ERROR_ARGS, args = {"tag"})
    public Result<IPage<Tag>> queryTagList(ListTagRequest request) {
        return tagService.listTag(request);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiException(value = Status.DELETE_ERROR_ARGS, args = {"tag"})
    public Result<?> deleteTag(@PathVariable("id") int id) {
        return tagService.deleteTag(id);
    }

    @PostMapping("/batch_update")
    public Result<?> updateTagSubject(@RequestBody UpdateTagSubjectRequest request) {
        return subjectService.updateTagSubject(request);
    }
}
