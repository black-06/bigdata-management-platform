package com.github.bmp.commons.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum DatasourceType {
    MYSQL(1),
    HIVE(2),
    ;

    @EnumValue
    private final int code;

    DatasourceType(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static DatasourceType ofName(String name) {
        return Arrays.stream(DatasourceType.values())
                .filter(type -> StringUtils.equalsIgnoreCase(type.name(), name))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("no such datasource type"));
    }
}
