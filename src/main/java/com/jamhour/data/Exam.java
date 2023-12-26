package com.jamhour.data;

import com.jamhour.database.Database;
import com.jamhour.database.TableColumn;
import com.jamhour.database.TableColumnImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public record Exam(String name, String description, LocalDateTime examDateTime, int id,
                   int courseId) implements Comparable<Exam> {
    private static final Comparator<Exam> COMPARATOR =
            Comparator
                    .comparingInt(Exam::id)
                    .thenComparing(Exam::courseId)
                    .thenComparing(Exam::examDateTime)
                    .thenComparing(Exam::name)
                    .thenComparing(Exam::description);

    public static String getInsertColumns() {
        return Column.getInsertColumns();
    }

    public static String getInsertValues(Exam thingToInsert) {
        return Column.getInsertValues(thingToInsert);
    }

    @Override
    public int compareTo(Exam o) {
        return COMPARATOR.compare(this, o);
    }

    public static Map<TableColumn, String> getTableColumns() {
        return Column.toMap();
    }

    public static Exam get(ResultSet resultSet) throws SQLException {
        return new Exam(
                resultSet.getString(Exam.Column.NAME.columnName()),
                resultSet.getString(Exam.Column.DESCRIPTION.columnName()),
                resultSet.getTimestamp(Exam.Column.EXAM_DATE_TIME.columnName()).toLocalDateTime(),
                resultSet.getInt(Exam.Column.ID.columnName()),
                resultSet.getInt(Exam.Column.COURSE_ID.columnName())
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
        DESCRIPTION(
                TableColumnImpl.builder()
                        .columnName("description")
                        .type(String.class)
                        .isNullable(true)
                        .build()
        ),
        EXAM_DATE_TIME(
                TableColumnImpl.builder()
                        .columnName("date_time")
                        .type(LocalDateTime.class)
                        .build()
        ),
        ID(
                TableColumnImpl.builder()
                        .columnName("id")
                        .type(Integer.class)
                        .isPrimaryKey(true)
                        .build()
        ),
        COURSE_ID(
                TableColumnImpl.builder()
                        .columnName("course_id")
                        .type(Integer.class)
                        .isForeignKey(true)
                        .build()
        );

        private final TableColumn tableColumn;

        public static String getInsertValues(Exam thingToInsert) {
            return "%s,%s,%s,%s,%s"
                    .formatted(
                            Database.getInstance().enquoteLiteral(thingToInsert.name()),
                            Database.getInstance().enquoteLiteral(thingToInsert.description()),
                            Database.getInstance().enquoteLiteral(String.valueOf(thingToInsert.examDateTime())),
                            Database.getInstance().enquoteLiteral(String.valueOf(thingToInsert.id())),
                            Database.getInstance().enquoteLiteral(String.valueOf(thingToInsert.courseId()))
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
                                                         Exam.Column column,
                                                         Object thingToSet) throws SQLException {
            return switch (column) {
                case ID, COURSE_ID -> {
                    preparedStatement.setInt(1, (int) thingToSet);
                    yield preparedStatement;
                }
                case NAME, DESCRIPTION -> {
                    preparedStatement.setString(1, (String) thingToSet);
                    yield preparedStatement;
                }
                case EXAM_DATE_TIME -> {
                    preparedStatement.setTimestamp(1, java.sql.Timestamp.valueOf((LocalDateTime) thingToSet));
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
                case ID, COURSE_ID -> preparedStatement.setInt(1, (int) valueToSet);
                case NAME, DESCRIPTION -> preparedStatement.setString(1, (String) valueToSet);
                case EXAM_DATE_TIME ->
                        preparedStatement.setTimestamp(1, java.sql.Timestamp.valueOf((LocalDateTime) valueToSet));
            }

            switch (columnToCompare) {
                case ID, COURSE_ID -> preparedStatement.setInt(2, (int) valueToCompare);
                case NAME, DESCRIPTION -> preparedStatement.setString(2, (String) valueToCompare);
                case EXAM_DATE_TIME ->
                        preparedStatement.setTimestamp(2, java.sql.Timestamp.valueOf((LocalDateTime) valueToCompare));
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
                    Column.COURSE_ID.toEntry(),
                    Column.NAME.toEntry(),
                    Column.DESCRIPTION.toEntry(),
                    Column.EXAM_DATE_TIME.toEntry()
            );
        }
    }
}
