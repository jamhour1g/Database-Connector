package com.jamhour.data;

import com.jamhour.database.Database;
import com.jamhour.database.TableColumn;
import com.jamhour.database.TableColumnImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public record Course(String name, int id, int teacherId) implements Comparable<Course> {

    private static final Comparator<Course> COMPARATOR =
            Comparator
                    .comparingInt(Course::id)
                    .thenComparing(Course::teacherId)
                    .thenComparing(Course::name);

    public static String getInsertColumns() {
        return Column.getInsertColumns();
    }

    public static String getInsertValues(Course thingToInsert) {
        return Column.getInsertValues(thingToInsert);
    }


    @Override
    public int compareTo(Course other) {
        return COMPARATOR.compare(this, other);
    }

    public static Map<TableColumn, String> getTableColumns() {
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
        NAME(
                TableColumnImpl.builder()
                        .columnName("name")
                        .type(String.class)
                        .build()
        ),
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
        );

        private final TableColumn tableColumn;

        public static String getInsertValues(Course thingToInsert) {
            return "%s,%s,%s"
                    .formatted(
                            Database.getInstance().enquoteLiteral(thingToInsert.name()),
                            Database.getInstance().enquoteLiteral(String.valueOf(thingToInsert.id())),
                            Database.getInstance().enquoteLiteral(String.valueOf(thingToInsert.teacherId()))
                    );

        }

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

        public static PreparedStatement setColumnDetails(PreparedStatement preparedStatement,
                                                         Course.Column column,
                                                         Object thingToSet) throws SQLException {
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

        public static PreparedStatement setColumnDetails(PreparedStatement preparedStatement,
                                                         Column columnToSet,
                                                         Object valueToSet,
                                                         Column columnToCompare,
                                                         Object valueToCompare) throws SQLException {

            switch (columnToSet) {
                case ID, TEACHER_ID -> preparedStatement.setInt(1, (int) valueToSet);
                case NAME -> preparedStatement.setString(1, (String) valueToSet);
            }

            switch (columnToCompare) {
                case ID, TEACHER_ID -> preparedStatement.setInt(2, (int) valueToCompare);
                case NAME -> preparedStatement.setString(2, (String) valueToCompare);
            }

            return preparedStatement;
        }

        public static String getInsertColumns() {
            return Arrays.stream(values())
                    .map(Column::columnName)
                    .collect(Collectors.joining(","));
        }

        private Map.Entry<TableColumn, String> toEntry() {
            return Map.entry(this, columnName());
        }

        private static Map<TableColumn, String> toMap() {
            return Map.ofEntries(
                    Column.ID.toEntry(),
                    Column.TEACHER_ID.toEntry(),
                    Column.NAME.toEntry()
            );
        }
    }
}
