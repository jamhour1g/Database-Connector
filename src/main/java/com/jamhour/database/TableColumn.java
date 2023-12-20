package com.jamhour.database;

public interface TableColumn<T> {

    String name();
    Class<T> getType();

    boolean isPrimaryKey();
    boolean isNullable();
}
