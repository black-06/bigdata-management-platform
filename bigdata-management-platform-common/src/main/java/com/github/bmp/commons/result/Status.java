package com.github.bmp.commons.result;

public enum Status {
    SUCCESS(0, "success"),
    INTERNAL_SERVER_ERROR_ARGS(10000, "internal server error: {0}"),
    INVALID_PARAM_ARGS(10001, "invalid param: {0}"),
    CREATE_ERROR_ARGS(10010, "create {0} error"),
    UPDATE_ERROR_ARGS(10011, "update {0} error"),
    QUERY_ERROR_ARGS(10012, "query {0} error"),
    DELETE_ERROR_ARGS(10012, "query {0} error"),
    SEARCH_ERROR_ARGS(10013, "search error: {0}"),
    META_SYNC_ERROR_ARGS(10014, "meta sync error: {0}"),
    ;


    private final int code;
    private final String msg;

    Status(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
