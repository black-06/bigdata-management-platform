package org.example.connector.api.alignment;

public class AlignmentByIndex<A extends IColumn, B extends IColumn> extends Alignment<A, B> {
    @Override
    public AlignmentResult<A, B> align(A[] listA, B[] listB) {
        AlignmentResult<A, B> result = AlignmentResult.empty();
        int index = 0;
        for (; index < listA.length && index < listB.length; index++) {
            if (!super.compareFunc.apply(index, index, listA[index], listB[index])) {
                break;
            }
        }
        for (int i = 0; i < listA.length; i++) {
            if (i <= index) {
                result.sameA.add(listA[i]);
            } else {
                result.extraA.add(listA[i]);
            }
        }
        for (int i = 0; i < listB.length; i++) {
            if (i <= index) {
                result.sameB.add(listB[i]);
            } else {
                result.extraB.add(listB[i]);
            }
        }
        return result;
    }
}
