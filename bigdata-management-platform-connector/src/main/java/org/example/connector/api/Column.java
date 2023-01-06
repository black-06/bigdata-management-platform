package org.example.connector.api;

import lombok.Data;
import org.example.connector.api.alignment.IColumn;

@Data
public class Column implements IColumn {
    private String name;
    private String type;
}
