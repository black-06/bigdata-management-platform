package com.bmp.search.core;

import lombok.Data;

import java.time.Instant;

/**
 * Period is a length of time used to filter search result.
 * It's either defined by start time and end time or a time duration.
 */
@Data
public class Period {
    private Instant start;
    private Instant end;
}