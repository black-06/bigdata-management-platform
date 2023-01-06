package org.example.api.utils.exceptions;

import org.example.api.utils.result.Status;

import java.text.MessageFormat;


/**
 * service exception
 */
public class ServiceException extends RuntimeException {

    /**
     * code
     */
    private int code;

    public ServiceException() {
    }

    public ServiceException(Status status) {
        super(status.getMsg());
        this.code = status.getCode();
    }

    public ServiceException(Status status, Object... formatter) {
        super(MessageFormat.format(status.getMsg(), formatter));
        this.code = status.getCode();
    }

    public ServiceException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Exception cause) {
        super(message, cause);
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
