package com.jamhour.database;

import lombok.Builder;

@Builder
public record TableColumnImpl(
        String columnName,
        boolean isPrimaryKey,
        boolean isForeignKey,
        boolean isNullable,
        Class<?> type
) implements TableColumn {

    @Override
    public Class<?> getType() {
        return type();
    }
}
