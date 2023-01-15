package com.github.bmp.commons.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum FileType {
    NO(0),
    CSV(1),
    JSON(2),
    PARQUET(3),
    ORC(4),
    ;

    @EnumValue
    private final int code;

    FileType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
