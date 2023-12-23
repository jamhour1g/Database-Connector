package com.jamhour.data;

import com.jamhour.database.TableColumn;
import com.jamhour.database.TableColumnImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Map;

public record Course(String name, int id, int teacherId) implements Comparable<Course> {

    private static final Comparator<Course> COMPARATOR =
            Comparator
                    .comparingInt(Course::id)
                    .thenComparing(Course::teacherId)
                    .thenComparing(Course::name);


    @Override
    public int compareTo(Course other) {
        return COMPARATOR.compare(this, other);
    }

    public static Map<Enum<? extends TableColumn>, String> getTableColumns() {
        return Column.toMap();
    }

    public static Course get(ResultSet resultSet) throws SQLException {
        return new Course(
                resultSet.getString(Course.Column.NAME.columnName()),
                resultSet.getInt(Course.Column.ID.columnName()),
                resultSet.getInt(Course.Column.TEACHER_ID.columnName())
        );
    }

    @Getter
    @RequiredArgsConstructor
    public enum Column implements TableColumn {
        ID(
                TableColumnImpl.builder()
                        .columnName("id")
                        .type(Integer.class)
                        .isPrimaryKey(true)
                        .build()
        ),
        TEACHER_ID(
                TableColumnImpl.builder()
                        .columnName("teacher_id")
                        .type(Integer.class)
                        .isForeignKey(true)
                        .isNullable(true)
                        .build()
        ),
        NAME(
                TableColumnImpl.builder()
                        .columnName("name")
                        .type(String.class)
                        .build()
        );

        private final TableColumn tableColumn;

        @Override
        public String columnName() {
            return tableColumn.columnName();
        }

        @Override
        public Class<?> getType() {
            return tableColumn.getType();
        }


        @Override
        public boolean isPrimaryKey() {
            return tableColumn.isPrimaryKey();
        }

        @Override
        public boolean isNullable() {
            return tableColumn.isNullable();
        }

        @Override
        public boolean isForeignKey() {
            return tableColumn.isForeignKey();
        }

        public static <T> PreparedStatement setColumnDetails(PreparedStatement preparedStatement,
                                                             Course.Column column,
                                                             T thingToSet) throws SQLException {
            return switch (column) {
                case ID, TEACHER_ID -> {
                    preparedStatement.setInt(1, (int) thingToSet);
                    yield preparedStatement;
                }
                case NAME -> {
                    preparedStatement.setString(1, (String) thingToSet);
                    yield preparedStatement;
                }
            };
        }

        private Map.Entry<Enum<? extends TableColumn>, String> toEntry() {
            return Map.entry(this, columnName());
        }

        private static Map<Enum<? extends TableColumn>, String> toMap() {
            return Map.ofEntries(
                    Column.ID.toEntry(),
                    Column.TEACHER_ID.toEntry(),
                    Column.NAME.toEntry()
            );
        }
    }
}
