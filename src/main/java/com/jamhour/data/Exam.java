package com.jamhour.data;

import com.jamhour.database.Table;
import com.jamhour.database.TableColumn;
import com.jamhour.database.TableColumnImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Map;

public record Exam(String name, String description, LocalDateTime examDateTime, int id,
                   int courseId) implements Comparable<Exam>, Table {

    private static final Comparator<Exam> COMPARATOR =
            Comparator
                    .comparingInt(Exam::id)
                    .thenComparing(Exam::courseId)
                    .thenComparing(Exam::examDateTime)
                    .thenComparing(Exam::name)
                    .thenComparing(Exam::description);

    private static final TableColumn<Integer> EXAM_ID = TableColumnImpl.of("id", Integer.class, true);
    private static final TableColumn<Integer> COURSE_ID = TableColumnImpl.of("course_id", Integer.class);
    private static final TableColumn<String> EXAM_NAME = TableColumnImpl.of("name", String.class);
    private static final TableColumn<String> EXAM_DESCRIPTION = TableColumnImpl.of("description", String.class);
    private static final TableColumn<LocalDateTime> EXAM_DATE_TIME = TableColumnImpl.of("exam_date_time", LocalDateTime.class);

    @Override
    public int compareTo(Exam o) {
        return COMPARATOR.compare(this, o);
    }

    @Override
    public Map<TableColumn<?>, String> getTableColumns() {
        return Map.of(
                EXAM_ID, EXAM_ID.name(),
                COURSE_ID, COURSE_ID.name(),
                EXAM_NAME, EXAM_NAME.name(),
                EXAM_DESCRIPTION, EXAM_DESCRIPTION.name(),
                EXAM_DATE_TIME, EXAM_DATE_TIME.name()
        );
    }

    @Override
    public String getTableName() {
        return "exam";
    }

    @Override
    public int getPrimaryKey() {
        return id();
    }

    @Getter
    @RequiredArgsConstructor
    public enum ExamColumn {
        ID(Exam.EXAM_ID),
        COURSE_ID(Exam.COURSE_ID),
        NAME(Exam.EXAM_NAME),
        DESCRIPTION(Exam.EXAM_DESCRIPTION),
        DATE_TIME(Exam.EXAM_DATE_TIME);

        private final TableColumn<?> tableColumn;
    }
}
