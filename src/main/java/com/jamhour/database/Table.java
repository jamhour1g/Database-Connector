package com.jamhour.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface Table {

    Map<TableColumn, String> getTableColumns();

    String getTableName();

    default List<TableColumn> getPrimaryKeys() {
        return getTableColumns()
                .keySet()
                .stream()
                .filter(TableColumn::isPrimaryKey)
                .toList();
    }

    default List<TableColumn> getForeignKeys() {
        return getTableColumns()
                .keySet()
                .stream()
                .filter(TableColumn::isForeignKey)
                .toList();
    }

    <T> T getObject(ResultSet resultSet) throws SQLException;

    <T> PreparedStatement setColumnDetails(PreparedStatement preparedStatement, TableColumn column, T thingToSet) throws SQLException;

}
