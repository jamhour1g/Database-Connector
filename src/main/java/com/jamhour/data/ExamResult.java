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

public record ExamResult(double grade, int examId, int studentId) implements Comparable<ExamResult> {

    private static final Comparator<ExamResult> COMPARATOR =
            Comparator
                    .comparingInt(ExamResult::examId)
                    .thenComparingInt(ExamResult::studentId)
                    .thenComparingDouble(ExamResult::grade);

    public static Map<TableColumn, String> getTableColumns() {
        return Column.toMap();
    }

    @Override
    public int compareTo(ExamResult other) {
        return COMPARATOR.compare(this, other);
    }

    public static ExamResult get(ResultSet resultSet) throws SQLException {
        return new ExamResult(
                resultSet.getDouble(ExamResult.Column.GRADE.columnName()),
                resultSet.getInt(ExamResult.Column.EXAM_ID.columnName()),
                resultSet.getInt(ExamResult.Column.STUDENT_ID.columnName())
        );
    }

    @Getter
    @RequiredArgsConstructor
    public enum Column implements TableColumn {
        STUDENT_ID(
                TableColumnImpl.builder()
                        .columnName("student_id")
                        .type(Integer.class)
                        .isPrimaryKey(true)
                        .isForeignKey(true)
                        .build()
        ),
        GRADE(
                TableColumnImpl.builder()
                        .columnName("grade")
                        .type(Double.class)
                        .isNullable(true)
                        .build()
        ),
        EXAM_ID(
                TableColumnImpl.builder()
                        .columnName("exam_id")
                        .type(Integer.class)
                        .isPrimaryKey(true)
                        .isForeignKey(true)
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

        public static <T> PreparedStatement setColumnDetails(PreparedStatement preparedStatement,
                                                             ExamResult.Column column,
                                                             T thingToSet) throws SQLException {
            return switch (column) {
                case EXAM_ID, STUDENT_ID -> {
                    preparedStatement.setInt(1, (int) thingToSet);
                    yield preparedStatement;
                }
                case GRADE -> {
                    preparedStatement.setDouble(1, (double) thingToSet);
                    yield preparedStatement;
                }
            };
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

        private Map.Entry<TableColumn, String> toEntry() {
            return Map.entry(this, columnName());
        }

        private static Map<TableColumn, String> toMap() {
            return Map.ofEntries(
                    Column.STUDENT_ID.toEntry(),
                    Column.GRADE.toEntry(),
                    Column.EXAM_ID.toEntry()
            );
        }
    }
}
