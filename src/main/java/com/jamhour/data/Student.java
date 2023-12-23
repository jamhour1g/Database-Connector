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

        ID(
                TableColumnImpl.builder()
                        .columnName("id")
                        .type(Integer.class)
                        .isPrimaryKey(true)
                        .build()
        ),
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
                                                             Column column,
                                                             T thingToSet) throws SQLException {
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
