package com.bmp.connector.fake;

import com.bmp.connector.api.list.AssetPath;
import com.bmp.connector.api.list.Lister;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class FakeLister implements Lister {
    public FakeLister() {
    }

    @Override
    public List<Asset> list(AssetPath path) throws SQLException {
        return Collections.emptyList();
    }

    @Override
    public String buildPath(AssetPath path, boolean quote) {
        if (quote) {
            return path.getFullName(".", "`", "`");
        }
        return path.getFullName(".");
    }

    @Override
    public void close() throws Exception {
    }
}
