package com.bmp.connector.api.list;

import com.fasterxml.jackson.annotation.*;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Arrays;

/**
 * A database name and table name combo in datasource.
 */
@EqualsAndHashCode
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public class AssetPath implements Serializable {
    @JsonInclude
    private final String[] paths;

    @JsonCreator
    public AssetPath(@JsonProperty("paths") String... paths) {
        if (ArrayUtils.isEmpty(paths)) {
            this.paths = ArrayUtils.EMPTY_STRING_ARRAY;
        } else {
            this.paths = paths;
        }
    }

    @JsonGetter
    public @Nonnull String[] getPaths() {
        return paths;
    }

    public String getFullName(String sep) {
        return getFullName(sep, "", "");
    }

    public String getFullName(String sep, String prefix, String suffix) {
        if (paths.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < paths.length; i++) {
            if (i > 0) {
                builder.append(sep);
            }
            builder.append(prefix);
            builder.append(paths[i]);
            builder.append(suffix);
        }
        return builder.toString();
    }

    public String getName() {
        int index = paths.length - 1;
        if (index < 0) {
            return "";
        }
        return paths[index];
    }

    public AssetPath getParent() {
        int index = paths.length - 1;
        if (index < 1) {
            return new AssetPath();
        }
        return new AssetPath(ArrayUtils.remove(paths, index));
    }

    public AssetPath getChild(String name) {
        if (paths.length == 0) {
            return new AssetPath(name);
        }
        String[] child = Arrays.copyOf(paths, paths.length + 1);
        child[paths.length] = name;
        return new AssetPath(child);
    }
}
