package com.jamhour.database;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
public class TableColumnImpl<T> implements TableColumn<T> {

    private final String name;
    private final Class<T> type;
    private final boolean isPrimaryKey;
    private final boolean isNullable;

    public static <T> TableColumnImpl<T> of(String name, Class<T> type) {
        return new TableColumnImpl<>(name, type, false, false);
    }

    public String name() {
        return name;
    }

    @Override
    public Class<T> getType() {
        return type;
    }

    @Override
    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    @Override
    public boolean isNullable() {
        return isNullable;
    }
}