package com.bmp.connector.fake;

import com.bmp.connector.api.Connector;
import com.bmp.connector.api.RowIterator;
import com.bmp.connector.api.alignment.Alignment;
import com.bmp.connector.api.alignment.AlignmentByName;
import com.bmp.connector.api.alignment.IColumn;
import com.bmp.connector.api.list.Lister;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FakeConnector implements Connector {
    @Override
    public void ping() {
    }

    @Override
    public Lister getLister() {
        return new FakeLister();
    }

    @Override
    public <A extends IColumn, B extends IColumn> Alignment<A, B> getAlignment() {
        return new AlignmentByName<>();
    }

    @Override
    public RowIterator getRowIterator() {
        return new FakeRowIterator();
    }
}
