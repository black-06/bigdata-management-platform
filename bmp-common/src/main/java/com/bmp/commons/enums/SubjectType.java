package com.bmp.commons.enums;

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
    /**
     * database asset.
     */
    DATABASE(5),
    /**
     * table asset.
     */
    TABLE(6),
    /**
     * fileset asset.
     */
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
