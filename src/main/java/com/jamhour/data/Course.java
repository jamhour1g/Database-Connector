package com.jamhour.data;

import com.jamhour.database.Table;
import com.jamhour.database.TableColumn;
import com.jamhour.database.TableColumnImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.Map;

public record Course(String name, int id, int teacherId) implements Comparable<Course>, Table {

    private static final TableColumn<Integer> COURSE_ID = TableColumnImpl.of("id", Integer.class, true);
    private static final TableColumn<Integer> TEACHER_ID = TableColumnImpl.of("teacher_id", Integer.class);
    private static final TableColumn<String> COURSE_NAME = TableColumnImpl.of("name", String.class);

    private static final Comparator<Course> COMPARATOR =
            Comparator
                    .comparingInt(Course::id)
                    .thenComparing(Course::teacherId)
                    .thenComparing(Course::name);


    @Override
    public int compareTo(Course other) {
        return COMPARATOR.compare(this, other);
    }

    @Override
    public Map<TableColumn<?>, String> getTableColumns() {
        return Map.of(
                COURSE_ID, COURSE_ID.name(),
                TEACHER_ID, TEACHER_ID.name(),
                COURSE_NAME, COURSE_NAME.name()
        );
    }

    @Override
    public String getTableName() {
        return "course";
    }

    @Override
    public int getPrimaryKey() {
        return id();
    }

    @Getter
    @RequiredArgsConstructor
    public enum CourseColumn {
        ID(Course.COURSE_ID),
        TEACHER_ID(Course.TEACHER_ID),
        NAME(Course.COURSE_NAME);

        private final TableColumn<?> tableColumn;
    }
}
