package com.jamhour.data;

import com.jamhour.database.TableColumn;
import com.jamhour.database.TableColumnImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;

public record Teacher(String name, String phone, String email, String major, double salary, int experience,
                      LocalDate dateOfBirth, int id) implements Comparable<Teacher> {
    private static final Comparator<Teacher> COMPARATOR =
            Comparator
                    .comparing(Teacher::id)
                    .thenComparing(Teacher::phone)
                    .thenComparing(Teacher::email)
                    .thenComparing(Teacher::dateOfBirth)
                    .thenComparing(Teacher::name)
                    .thenComparing(Teacher::experience)
                    .thenComparing(Teacher::salary);

    public static Map<TableColumn, String> getTableColumns() {
        return Column.toMap();
    }

    @Override
    public int compareTo(Teacher other) {
        return COMPARATOR.compare(this, other);
    }

    public static Teacher get(ResultSet resultSet) throws SQLException {
        return new Teacher(
                resultSet.getString(Teacher.Column.NAME.columnName()),
                resultSet.getString(Teacher.Column.PHONE.columnName()),
                resultSet.getString(Teacher.Column.EMAIL.columnName()),
                resultSet.getString(Teacher.Column.MAJOR.columnName()),
                resultSet.getDouble(Teacher.Column.SALARY.columnName()),
                resultSet.getInt(Teacher.Column.EXPERIENCE.columnName()),
                resultSet.getDate(Teacher.Column.DATE_OF_BIRTH.columnName()).toLocalDate(),
                resultSet.getInt(Teacher.Column.ID.columnName())
        );
    }

    @Getter
    @RequiredArgsConstructor
    public enum Column implements TableColumn {

        NAME(
                TableColumnImpl.builder()
                        .type(String.class)
                        .columnName("name")
                        .build()
        ),
        PHONE(
                TableColumnImpl.builder()
                        .type(String.class)
                        .columnName("phone")
                        .build()
        ),
        EMAIL(
                TableColumnImpl.builder()
                        .type(String.class)
                        .columnName("email")
                        .build()
        ),
        MAJOR(
                TableColumnImpl.builder()
                        .type(String.class)
                        .columnName("major")
                        .build()
        ),
        SALARY(
                TableColumnImpl.builder()
                        .type(Double.class)
                        .columnName("salary")
                        .build()
        ),
        EXPERIENCE(
                TableColumnImpl.builder()
                        .type(Integer.class)
                        .columnName("experience")
                        .build()
        ),
        DATE_OF_BIRTH(
                TableColumnImpl.builder()
                        .type(LocalDate.class)
                        .columnName("date_of_birth")
                        .build()
        ),
        ID(
                TableColumnImpl.builder()
                        .type(Integer.class)
                        .columnName("id")
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

        public static <T> PreparedStatement setColumnDetails(PreparedStatement preparedStatement,
                                                             Teacher.Column column,
                                                             T thingToSet) throws SQLException {
            return switch (column) {
                case ID, EXPERIENCE -> {
                    preparedStatement.setInt(1, (int) thingToSet);
                    yield preparedStatement;
                }
                case NAME, EMAIL, PHONE, MAJOR -> {
                    preparedStatement.setString(1, (String) thingToSet);
                    yield preparedStatement;
                }
                case SALARY -> {
                    preparedStatement.setDouble(1, (double) thingToSet);
                    yield preparedStatement;
                }
                case DATE_OF_BIRTH -> {
                    preparedStatement.setDate(1, java.sql.Date.valueOf((LocalDate) thingToSet));
                    yield preparedStatement;
                }
            };
        }

        private Map.Entry<TableColumn, String> toEntry() {
            return Map.entry(this, columnName());
        }

        private static Map<TableColumn, String> toMap() {
            return Map.ofEntries(
                    Column.NAME.toEntry(),
                    Column.PHONE.toEntry(),
                    Column.EMAIL.toEntry(),
                    Column.MAJOR.toEntry(),
                    Column.SALARY.toEntry(),
                    Column.EXPERIENCE.toEntry(),
                    Column.DATE_OF_BIRTH.toEntry(),
                    Column.ID.toEntry()
            );
        }
    }
}
