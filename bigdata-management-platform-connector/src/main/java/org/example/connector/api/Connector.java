package org.example.connector.api;

import org.example.connector.api.alignment.Alignment;
import org.example.connector.api.alignment.IColumn;

import java.sql.SQLException;
import java.util.List;

public interface Connector {
    void ping() throws SQLException;

    List<Asset> listAssets(AssetPath path) throws SQLException;

    <A extends IColumn, B extends IColumn> Alignment<A, B> getAlignment();
}


