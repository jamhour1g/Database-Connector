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

public record Enrollment(EnrollmentStatus status, boolean payed, int courseId, int studentId)
        implements Comparable<Enrollment> {

    private static final Comparator<Enrollment> COMPARATOR =
            Comparator
                    .comparingInt(Enrollment::studentId)
                    .thenComparingInt(Enrollment::courseId)
                    .thenComparing(Enrollment::payed)
                    .thenComparing(Enrollment::status);


    public static Map<TableColumn, String> getTableColumns() {
        return Column.toMap();
    }

    public static String getInsertColumns() {
        return Column.getInsertColumns();
    }

    public static String getInsertValues(Enrollment thingToInsert) {
        return Column.getInsertValues(thingToInsert);
    }

    @Override
    public int compareTo(Enrollment other) {
        return COMPARATOR.compare(this, other);
    }

    public static Enrollment get(ResultSet resultSet) throws SQLException {
        return new Enrollment(
                EnrollmentStatus.valueOf(resultSet.getString(Column.STATUS.columnName()).toUpperCase()),
                resultSet.getBoolean(Column.PAID.columnName()),
                resultSet.getInt(Column.COURSE_ID.columnName()),
                resultSet.getInt(Column.STUDENT_ID.columnName())
        );
    }

    @Getter
    @RequiredArgsConstructor
    public enum EnrollmentStatus {

        ACTIVE("active"),
        COMPLETED("completed"),
        DROPPED("dropped"),
        WITHDRAWN("withdrawn");

        private final String status;

    }

    @Getter
    @RequiredArgsConstructor
    public enum Column implements TableColumn {

        STATUS(
                TableColumnImpl.builder()
                        .columnName("enrollment_status")
                        .type(EnrollmentStatus.class)
                        .isNullable(true)
                        .build()
        ),
        PAID(
                TableColumnImpl.builder()
                        .columnName("paid")
                        .type(Boolean.class)
                        .isNullable(true)
                        .build()
        ),
        COURSE_ID(
                TableColumnImpl.builder()
                        .columnName("course_id")
                        .type(Integer.class)
                        .isForeignKey(true)
                        .isForeignKey(true)
                        .build()
        ),
        STUDENT_ID(
                TableColumnImpl.builder()
                        .columnName("student_id")
                        .type(Integer.class)
                        .isForeignKey(true)
                        .isForeignKey(true)
                        .build()
        );

        private final TableColumn tableColumn;

        public static String getInsertValues(Enrollment thingToInsert) {
            return "%s,%s,%s,%s"
                    .formatted(
                            Database.getInstance().enquoteLiteral(thingToInsert.status().toString().toLowerCase()),
                            Database.getInstance().enquoteLiteral(String.valueOf(thingToInsert.payed() ? 1 : 0)),
                            Database.getInstance().enquoteLiteral(String.valueOf(thingToInsert.courseId())),
                            Database.getInstance().enquoteLiteral(String.valueOf(thingToInsert.studentId()))
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
                                                         Column column,
                                                         Object thingToSet) throws SQLException {
            return switch (column) {
                case STATUS -> {
                    preparedStatement.setString(1, (String) thingToSet);
                    yield preparedStatement;
                }
                case PAID -> {
                    preparedStatement.setBoolean(1, (boolean) thingToSet);
                    yield preparedStatement;
                }
                case COURSE_ID, STUDENT_ID -> {
                    preparedStatement.setInt(1, (int) thingToSet);
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
                case STATUS -> preparedStatement.setString(1, (String) valueToSet);
                case PAID -> preparedStatement.setBoolean(1, (boolean) valueToSet);
                case COURSE_ID, STUDENT_ID -> preparedStatement.setInt(1, (int) valueToSet);
            }

            switch (columnToCompare) {
                case STATUS -> preparedStatement.setString(1, (String) valueToCompare);
                case PAID -> preparedStatement.setBoolean(1, (boolean) valueToCompare);
                case COURSE_ID, STUDENT_ID -> preparedStatement.setInt(1, (int) valueToCompare);
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
                    Column.STATUS.toEntry(),
                    Column.PAID.toEntry(),
                    Column.COURSE_ID.toEntry(),
                    Column.STUDENT_ID.toEntry()
            );
        }
    }
}
