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

public record ExamResult(double grade, int examId, int studentId) implements Comparable<ExamResult>, Table {

    public static final String TABLE_NAME = Schema.Tables.EXAM_RESULT.getTableName();
    private static final Comparator<ExamResult> COMPARATOR =
            Comparator
                    .comparingInt(ExamResult::examId)
                    .thenComparingInt(ExamResult::studentId)
                    .thenComparingDouble(ExamResult::grade);

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
        return examId();
    }

    @Override
    public int compareTo(ExamResult other) {
        return COMPARATOR.compare(this, other);
    }

    public static ExamResult get(ResultSet resultSet) throws SQLException {
        return new ExamResult(
                resultSet.getDouble(ExamResult.Column.GRADE.getName()),
                resultSet.getInt(ExamResult.Column.EXAM_ID.getName()),
                resultSet.getInt(ExamResult.Column.STUDENT_ID.getName())
        );
    }

    @Getter
    @RequiredArgsConstructor
    public enum Column {
        STUDENT_ID(TableColumnImpl.of("student_id", Integer.class, true)),
        GRADE(TableColumnImpl.of("grade", Integer.class)),
        EXAM_ID(TableColumnImpl.of("exam_id", Integer.class, true));

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
                    Column.STUDENT_ID.toEntry(),
                    Column.GRADE.toEntry(),
                    Column.EXAM_ID.toEntry()
            );
        }
    }
}
