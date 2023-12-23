package com.jamhour.database;

public interface TableColumn {
    String columnName();

    Class<?> getType();

    boolean isPrimaryKey();

    boolean isNullable();

    boolean isForeignKey();

}
