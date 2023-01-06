package org.example.api.dao.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum DatasourceType {
    MYSQL(1),
    ;

    @EnumValue
    private final int code;

    DatasourceType(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
