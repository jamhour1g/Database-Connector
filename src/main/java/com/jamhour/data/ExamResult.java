package com.jamhour.data;

import com.jamhour.database.Table;
import com.jamhour.database.TableColumn;
import com.jamhour.database.TableColumnImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.Map;

public record ExamResult(double grade, int examId, int studentId) implements Comparable<ExamResult>, Table {

    private static final Comparator<ExamResult> COMPARATOR =
            Comparator
                    .comparingInt(ExamResult::examId)
                    .thenComparingInt(ExamResult::studentId)
                    .thenComparingDouble(ExamResult::grade);

    @Override
    public Map<TableColumn<?>, String> getTableColumns() {
        return Map.of(
                Column.ID.getTableColumn(), Column.ID.getName(),
                Column.GRADE.getTableColumn(), Column.GRADE.getName(),
                Column.EXAM_ID.getTableColumn(), Column.EXAM_ID.getName()
        );
    }

    @Override
    public String getTableName() {
        return "exam_result";
    }

    @Override
    public int getPrimaryKey() {
        return examId();
    }

    @Override
    public int compareTo(ExamResult other) {
        return COMPARATOR.compare(this, other);
    }

    @Getter
    @RequiredArgsConstructor
    public enum Column {
        ID(TableColumnImpl.of("id", Integer.class)),
        GRADE(TableColumnImpl.of("grade", Integer.class)),
        EXAM_ID(TableColumnImpl.of("exam_id", Integer.class));

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
    }
}
