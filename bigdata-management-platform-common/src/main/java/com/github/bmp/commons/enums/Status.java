package com.github.bmp.commons.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum Status {
    ACTIVE(1),
    HIBERNATING(2),
    ;

    @EnumValue
    private final int code;

    Status(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
