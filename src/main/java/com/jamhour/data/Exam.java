package com.jamhour.data;

import com.jamhour.database.TableColumn;
import com.jamhour.database.TableColumnImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Map;

public record Exam(String name, String description, LocalDateTime examDateTime, int id,
                   int courseId) implements Comparable<Exam> {
    private static final Comparator<Exam> COMPARATOR =
            Comparator
                    .comparingInt(Exam::id)
                    .thenComparing(Exam::courseId)
                    .thenComparing(Exam::examDateTime)
                    .thenComparing(Exam::name)
                    .thenComparing(Exam::description);

    @Override
    public int compareTo(Exam o) {
        return COMPARATOR.compare(this, o);
    }

    public static Map<Enum<? extends TableColumn>, String> getTableColumns() {
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
        ),
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
                                                             Exam.Column column,
                                                             T thingToSet) throws SQLException {
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

        private Map.Entry<Enum<? extends TableColumn>, String> toEntry() {
            return Map.entry(this, columnName());
        }

        private static Map<Enum<? extends TableColumn>, String> toMap() {
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
