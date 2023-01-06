package org.example.connector.api;

import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * A database name and table name combo in datasource.
 */
@EqualsAndHashCode
public class AssetPath implements Serializable {
    /**
     * Quote prefix byte. e.g. "`" in MySQL, "[" in SQLServer.
     */
    private final String prefix;
    /**
     * Quote suffix byte. e.g. "`" in MySQL, "]" in SQLServer.
     */
    private final String suffix;
    /**
     * Separator. e.g. "." in database, "/" in unix.
     */
    private final String sep;
    private final String[] names;

    public AssetPath(String prefix, String suffix, String sep, String... names) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.sep = sep;
        this.names = names;
    }


    public String getFullName(boolean quote) {
        if (quote) {
            return Arrays.stream(names).map(s -> prefix + s + suffix).collect(Collectors.joining(sep));
        }
        return String.join(sep, names);
    }
}
