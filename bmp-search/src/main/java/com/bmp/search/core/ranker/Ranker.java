package com.bmp.search.core.ranker;

import com.bmp.search.core.Item;

import java.util.Comparator;

/**
 * rank search items based on:
 * <li>text relevance</li>
 * <li>create time</li>
 * <li>update time</li>
 */
public interface Ranker extends Comparator<Item> {
}
