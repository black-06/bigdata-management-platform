package com.bmp.search.core.packer;

import com.bmp.search.core.Item;

import java.util.List;

/**
 * packs search items with extra information.
 */
public interface Packer {
    List<Item> pack(List<Item> items);
}
