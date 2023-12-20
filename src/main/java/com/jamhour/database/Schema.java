package com.jamhour.database;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum Schema {
    ;

    public static final String SCHEMA_NAME = "EducationDB";

    @Getter
    public enum Tables {
        TEACHER("teacher"),
        STUDENT("student"),
        COURSE("course"),
        ENROLLMENT("enrollment"),
        EXAM("exam"),
        EXAM_RESULT("exam_result");

        private final String tableName;
        private final List<Column> columns;

        Tables(String tableName) {
            this.tableName = tableName;
            this.columns = getColumns();

        }

        public List<Column> getColumns() {
            return switch (this) {
                case TEACHER -> List.of(
                        new Column("id", Integer.class),
                        new Column("date_of_birth", LocalDate.class),
                        new Column("name", String.class),
                        new Column("email", String.class),
                        new Column("phone", String.class),
                        new Column("salary", Double.class),
                        new Column("experience", Integer.class),
                        new Column("major", String.class)

                );
                case STUDENT -> List.of(
                        new Column("id", Integer.class),
                        new Column("name", String.class),
                        new Column("email", String.class),
                        new Column("phone", String.class)
                );
                case COURSE -> List.of(
                        new Column("id", Integer.class),
                        new Column("name", String.class),
                        new Column("teacher_id", Integer.class)
                );
                case EXAM -> List.of(
                        new Column("id", Integer.class),
                        new Column("course_id", Integer.class),
                        new Column("name", String.class),
                        new Column("date_time", LocalDateTime.class),
                        new Column("description", String.class)
                );
                case EXAM_RESULT -> List.of(
                        new Column("exam_id", Integer.class),
                        new Column("student_id", Integer.class),
                        new Column("grade", Double.class)
                );
                case ENROLLMENT -> List.of(
                        new Column("student_id", Integer.class),
                        new Column("course_id", Integer.class),
                        new Column("has_paid", Boolean.class),
                        new Column("enrollment_status", EnrollmentStatus.class)
                );
            };
        }

        public record Column(String name, Class<?> type) {}
    }
}
