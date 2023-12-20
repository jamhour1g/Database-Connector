package com.jamhour.database;

import java.util.Map;

public interface Table {

    Map<TableColumn<?>, String> getTableColumns();

    String getTableName();

    int getPrimaryKey();

}
