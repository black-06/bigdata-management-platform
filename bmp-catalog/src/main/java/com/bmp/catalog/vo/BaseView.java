package com.bmp.catalog.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class BaseView {
    private List<TagView> tags;
}
