package org.example.connector.api.alignment;

import java.util.HashMap;
import java.util.Map;

public class AlignmentByName<A extends IColumn, B extends IColumn> extends Alignment<A, B> {
    @Override
    public AlignmentResult<A, B> align(A[] listA, B[] listB) {
        AlignmentResult<A, B> result = AlignmentResult.empty();
        Map<String, idxColumn<B>> mapB = new HashMap<>(listB.length);
        for (int i = 0; i < listB.length; i++) {
            mapB.put(listB[i].getName(), new idxColumn<>(i, listB[i]));
        }
        Map<String, idxColumn<A>> mapA = new HashMap<>(listA.length);
        for (int ai = 0; ai < listA.length; ai++) {
            A a = listA[ai];
            idxColumn<B> b = mapB.get(a.getName());
            if (b != null && super.compareFunc.apply(ai, b.index, a, b.column)) {
                result.sameA.add(a);
                result.sameB.add(b.column);
                mapA.put(a.getName(), new idxColumn<>(ai, a));
            } else {
                result.extraA.add(a);
            }
        }
        for (int bi = 0; bi < listB.length; bi++) {
            B b = listB[bi];
            idxColumn<A> a = mapA.get(b.getName());
            if (a != null && super.compareFunc.apply(a.index, bi, a.column, b)) {
                result.extraB.add(b);
            }
        }
        return result;
    }
}

class idxColumn<T extends IColumn> {
    final int index;
    final T column;

    idxColumn(int index, T column) {
        this.index = index;
        this.column = column;
    }
}

