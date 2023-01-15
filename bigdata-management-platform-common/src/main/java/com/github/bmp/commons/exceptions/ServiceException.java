package com.github.bmp.commons.exceptions;

/**
 * service exception
 */
public class ServiceException extends RuntimeException {

    /**
     * code
     */
    private int code;

    public ServiceException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
