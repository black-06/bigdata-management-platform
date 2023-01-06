package org.example.connector.api.alignment;

import java.util.ArrayList;
import java.util.List;

public class AlignmentResult<A extends IColumn, B extends IColumn> {
    public List<A> sameA;
    public List<A> extraA;
    public List<B> sameB;
    public List<B> extraB;

    public AlignmentResult(List<A> sameA, List<A> extraA, List<B> sameB, List<B> extraB) {
        this.sameA = sameA;
        this.extraA = extraA;
        this.sameB = sameB;
        this.extraB = extraB;
    }

    static <A extends IColumn, B extends IColumn> AlignmentResult<A, B> empty() {
        return new AlignmentResult<>(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }
}