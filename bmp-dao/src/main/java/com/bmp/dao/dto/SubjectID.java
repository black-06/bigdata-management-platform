package com.bmp.dao.dto;

import com.bmp.commons.enums.SubjectType;
import com.bmp.dao.entity.Subject;
import com.bmp.dao.entity.TagSubject;
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

    public static SubjectID of(TagSubject record) {
        return new SubjectID(record.getSubjectID(), record.getSubjectType());
    }
}
