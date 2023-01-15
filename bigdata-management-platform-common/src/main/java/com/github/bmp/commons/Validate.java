package com.github.bmp.commons;

import com.github.bmp.commons.exceptions.ServiceException;
import com.github.bmp.commons.result.Result;

public class Validate {
    public static <T> T notNull(final T object, final String message, final Object... values) {
        if (object == null) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return object;
    }

    public static void success(Result<?> result) {
        notNull(result, "result is null");
        if (!result.isSuccess()) {
            throw new ServiceException(result.getCode(), result.getMsg());
        }
    }
}
