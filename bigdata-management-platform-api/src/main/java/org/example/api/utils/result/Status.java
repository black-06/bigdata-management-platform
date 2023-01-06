package org.example.api.utils.result;

public enum Status {
    SUCCESS(0, "success"),
    INTERNAL_SERVER_ERROR_ARGS(10000, "internal server error: {0}"),
    CREATE_COLLECTION_ERROR(10001, "create collection error"),
    QUERY_COLLECTION_ERROR(10002, "query collection error"),

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
