package com.jamhour.database;

public interface TableColumn<T> {

    String getName();
    T getType();

    boolean isPrimaryKey();
    boolean isNullable();
}
