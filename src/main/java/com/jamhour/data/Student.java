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

public record Student(String name, String email, String phone, int id) implements Comparable<Student> {

    private static final Comparator<Student> COMPARATOR =
            Comparator
                    .comparingInt(Student::id)
                    .thenComparing(Student::phone)
                    .thenComparing(Student::name)
                    .thenComparing(Student::email);

    public static Map<TableColumn, String> getTableColumns() {
        return Column.toMap();
    }

    public static String getInsertColumns() {
        return Column.getInsertColumns();
    }

    public static String getInsertValues(Student student) {
        return Column.getInsertValues(student);
    }

    @Override
    public int compareTo(Student other) {
        return COMPARATOR.compare(this, other);
    }

    public static Student get(ResultSet resultSet) throws SQLException {
        return new Student(
                resultSet.getString(Column.NAME.columnName()),
                resultSet.getString(Column.EMAIL.columnName()),
                resultSet.getString(Column.PHONE.columnName()),
                resultSet.getInt(Column.ID.columnName())
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
        EMAIL(
                TableColumnImpl.builder()
                        .columnName("email")
                        .type(String.class)
                        .build()
        ),
        PHONE(
                TableColumnImpl.builder()
                        .columnName("phone")
                        .type(String.class)
                        .build()
        ),
        ID(
                TableColumnImpl.builder()
                        .columnName("id")
                        .type(Integer.class)
                        .isPrimaryKey(true)
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

        public static PreparedStatement setColumnDetails(PreparedStatement preparedStatement,
                                                         Column column,
                                                         Object thingToSet) throws SQLException {
            return switch (column) {
                case ID -> {
                    preparedStatement.setInt(1, (int) thingToSet);
                    yield preparedStatement;
                }
                case NAME, EMAIL, PHONE -> {
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
                case ID -> preparedStatement.setInt(1, (int) valueToSet);
                case NAME, EMAIL, PHONE -> preparedStatement.setString(1, (String) valueToSet);
            }

            switch (columnToCompare) {
                case ID -> preparedStatement.setInt(2, (int) valueToCompare);
                case NAME, EMAIL, PHONE -> preparedStatement.setString(2, (String) valueToCompare);
            }

            return preparedStatement;
        }

        public static String getInsertValues(Student student) {

            return "%s, %s, %s, %s"
                    .formatted(
                            Database.getInstance().enquoteLiteral(student.name()),
                            Database.getInstance().enquoteLiteral(student.email()),
                            Database.getInstance().enquoteLiteral(student.phone()),
                            Database.getInstance().enquoteLiteral(String.valueOf(student.id()))
                    );

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
                    Column.NAME.toEntry(),
                    Column.EMAIL.toEntry(),
                    Column.PHONE.toEntry()
            );
        }
    }
}
