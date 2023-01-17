package com.bmp.connector.api.list;

import com.bmp.commons.enums.AssetType;
import com.bmp.commons.enums.FileType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.SQLException;
import java.util.List;

public interface Lister extends AutoCloseable {
    List<Asset> list(AssetPath path) throws SQLException;

    String buildPath(AssetPath path, boolean quote);

    @Data
    @Accessors(chain = true)
    class Asset {
        private String name;
        private AssetType type;
        private Asset parent;
        private AssetPath path;
        private FileType fileType;
        private List<Column> columns;
    }

    @Data
    @Accessors(chain = true)
    class Column {
        private String Field;
        private String Type;
        private String Null;
        private String Key;
        private String Default;
        private String Extra;
        private String Comment;
    }
}
