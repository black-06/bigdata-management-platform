package com.bmp.commons.exceptions;

import com.bmp.commons.result.Status;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * controller exception annotation
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface ApiException {
    Status value();

    boolean msg() default false;

    String[] args() default {};
}
