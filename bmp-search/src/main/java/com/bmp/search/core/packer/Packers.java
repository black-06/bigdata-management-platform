package com.bmp.search.core.packer;

import com.bmp.search.core.Item;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class Packers implements Packer {
    public static final Packers Instance = new Packers(Arrays.asList(
            new SubjectPacker(),
            new TagPacker()
    ));

    private final List<Packer> packers;

    @Override
    public List<Item> pack(List<Item> items) {
        for (Packer packer : packers) {
            items = packer.pack(items);
        }
        return items;
    }
}
