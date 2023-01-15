package com.github.bmp.commons.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum AssetType {
    DATABASE(1),
    TABLE(2),
    FILESET(3),
    ;

    @EnumValue
    private final int code;

    AssetType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
