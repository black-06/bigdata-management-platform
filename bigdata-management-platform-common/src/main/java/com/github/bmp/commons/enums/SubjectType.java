package com.github.bmp.commons.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum SubjectType {
    COLLECTION(1),
    DATASOURCE(2),
    /**
     * Deprecated, use DATABASE, TABLE, FILESET instead. see {@link AssetType}
     */
    @Deprecated
    ASSET(3),
    COLUMN(4),

    DATABASE(5),
    TABLE(6),
    FILESET(7),
    ;

    @EnumValue
    private final int code;

    SubjectType(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
