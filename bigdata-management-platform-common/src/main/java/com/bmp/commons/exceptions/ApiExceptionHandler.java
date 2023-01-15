package com.bmp.commons.exceptions;

import com.bmp.commons.result.Result;
import com.bmp.commons.result.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

/**
 * Exception Handler
 */
@RestControllerAdvice
@ResponseBody
public class ApiExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(ServiceException.class)
    public Result<?> exceptionHandler(ServiceException e, HandlerMethod ignoredHm) {
        logger.error("ServiceException: ", e);
        return Result.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<?> exceptionHandler(Exception e, HandlerMethod hm) {
        ApiException ae = hm.getMethodAnnotation(ApiException.class);
        if (ae == null) {
            logger.error(e.getMessage(), e);
            return Result.error(Status.INTERNAL_SERVER_ERROR_ARGS, e.getMessage());
        }
        Result<Object> result;
        if (ae.msg()) {
            result = Result.error(ae.value(), e.getMessage());
        } else {
            result = Result.error(ae.value(), (Object[]) ae.args());
        }
        logger.error(result.getMsg(), e);
        return result;
    }
}

