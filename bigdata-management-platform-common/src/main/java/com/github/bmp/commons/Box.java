package com.github.bmp.commons;

public class Box {

    public static int unbox(Integer value) {
        if (value != null) {
            return value;
        }
        return 0;
    }

    public static boolean unbox(Boolean value) {
        if (value != null) {
            return value;
        }
        return false;
    }
}
