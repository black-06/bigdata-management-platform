package com.bmp.search.core.aggregator;

import com.bmp.commons.enums.DatasourceType;
import com.bmp.commons.enums.SubjectType;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AggregationTest {

    @Test
    void union() {
        Map<Integer, Integer> tag = new HashMap<>();
        tag.put(1, 1);
        tag.put(2, 2);

        Aggregation aggregation = new Aggregation()
                .setDatasourceType(Collections.singletonMap(DatasourceType.MYSQL, 1))
                .setSubjectType(Collections.singletonMap(SubjectType.COLUMN, 2))
                .setCollection(Collections.singletonMap(2, 2))
                .setDatasource(null)
                .setTag(tag);

        Map<Integer, Integer> otherTag = new HashMap<>();
        otherTag.put(2, 2);
        otherTag.put(3, 3);

        aggregation.union(new Aggregation()
                .setDatasourceType(null)
                .setSubjectType(Collections.singletonMap(SubjectType.COLUMN, 1))
                .setCollection(Collections.singletonMap(3, 3))
                .setDatasource(Collections.singletonMap(4, 4))
                .setTag(otherTag)
        );

        assertMapEquals(Collections.singletonMap(DatasourceType.MYSQL, 1), aggregation.getDatasourceType());
        assertMapEquals(Collections.singletonMap(SubjectType.COLUMN, 3), aggregation.getSubjectType());
        Map<Integer, Integer> expectedCollection = new HashMap<>();
        expectedCollection.put(2, 2);
        expectedCollection.put(3, 3);
        assertMapEquals(expectedCollection, aggregation.getCollection());
        assertMapEquals(Collections.singletonMap(4, 4), aggregation.getDatasource());
        Map<Integer, Integer> expectedTag = new HashMap<>();
        expectedTag.put(1, 1);
        expectedTag.put(2, 4);
        expectedTag.put(3, 3);
        assertMapEquals(expectedTag, aggregation.getTag());
    }

    static <K, V> void assertMapEquals(Map<K, V> expected, Map<K, V> actual) {
        if (expected == null) {
            assertNull(actual);
            return;
        }

        assertNotNull(actual);
        assertEquals(expected.size(), actual.size(), "map size not equal");
        expected.forEach((k, v) -> assertEquals(v, actual.get(k), "value not equal when key is " + k));
    }

    static void assertAggregationEquals(Aggregation a1, Aggregation a2) {
        assertMapEquals(a1.getDatasourceType(), a2.getDatasourceType());
        assertMapEquals(a1.getSubjectType(), a2.getSubjectType());
        assertMapEquals(a1.getCollection(), a2.getCollection());
        assertMapEquals(a1.getDatasource(), a2.getDatasource());
        assertMapEquals(a1.getTag(), a2.getTag());
    }
}