package com.jamhour.data;

import com.jamhour.database.Schema;
import com.jamhour.database.Table;
import com.jamhour.database.TableColumn;
import com.jamhour.database.TableColumnImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Map;

public record Course(String name, int id, int teacherId) implements Comparable<Course>, Table {

    public static final String TABLE_NAME = Schema.Tables.COURSE.getTableName();
    private static final Comparator<Course> COMPARATOR =
            Comparator
                    .comparingInt(Course::id)
                    .thenComparing(Course::teacherId)
                    .thenComparing(Course::name);


    @Override
    public int compareTo(Course other) {
        return COMPARATOR.compare(this, other);
    }

    @Override
    public Map<TableColumn<?>, String> getTableColumns() {
        return Column.toMap();
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public int getPrimaryKey() {
        return id();
    }

    public static Course get(ResultSet resultSet) throws SQLException {
        return new Course(
                resultSet.getString(Course.Column.NAME.getName()),
                resultSet.getInt(Course.Column.ID.getName()),
                resultSet.getInt(Course.Column.TEACHER_ID.getName())
        );
    }

    @Getter
    @RequiredArgsConstructor
    public enum Column {
        ID(TableColumnImpl.of("id", Integer.class, true)),
        TEACHER_ID(TableColumnImpl.of("teacher_id", Integer.class)),
        NAME(TableColumnImpl.of("name", String.class));

        private final TableColumn<?> tableColumn;

        private TableColumn<?> getTableColumn() {
            return tableColumn;
        }

        public Class<?> getType() {
            return tableColumn.getType();
        }

        public String getName() {
            return tableColumn.name();
        }

        public boolean isPrimaryKey() {
            return tableColumn.isPrimaryKey();
        }

        public boolean isNullable() {
            return tableColumn.isNullable();
        }

        private Map.Entry<TableColumn<?>, String> toEntry() {
            return Map.entry(getTableColumn(), getName());
        }

        private static Map<TableColumn<?>, String> toMap() {
            return Map.ofEntries(
                    Column.ID.toEntry(),
                    Column.TEACHER_ID.toEntry(),
                    Column.NAME.toEntry()
            );
        }
    }
}
