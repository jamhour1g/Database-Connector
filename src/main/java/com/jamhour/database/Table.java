package com.jamhour.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface Table {

    <T extends Enum<? extends TableColumn> & TableColumn> Map<T, String> getTableColumns();

    String getTableName();

    default List<? extends Enum<? extends TableColumn>> getPrimaryKeys() {
        return getTableColumns()
                .keySet()
                .stream()
                .filter(TableColumn::isPrimaryKey)
                .toList();
    }

    default List<? extends Enum<? extends TableColumn>> getForeignKeys() {
        return getTableColumns()
                .keySet()
                .stream()
                .filter(TableColumn::isForeignKey)
                .toList();
    }

    <T> T getObject(ResultSet resultSet) throws SQLException;

    <T> PreparedStatement setColumnDetails(PreparedStatement preparedStatement, Enum<? extends TableColumn> column, T thingToSet) throws SQLException;

}
