package com.bmp.catalog.dto;

import com.bmp.commons.enums.SubjectType;
import com.bmp.dao.entity.Subject;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class SubjectID {
    private int id;
    private SubjectType type;

    public static <T extends Subject> List<SubjectID> ofList(List<T> records) {
        return records.stream().map(SubjectID::of).collect(Collectors.toList());
    }

    public static <T extends Subject> SubjectID of(T record) {
        return new SubjectID(record.getId(), record.type());
    }
}
