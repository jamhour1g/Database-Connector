package com.jamhour.database;

import com.jamhour.data.EnrollmentStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum Schema {
    ;

    public static final String NAME = "EducationDB";

    static String getSchemaCreationQuery() {
        return STR."CREATE DATABASE IF NOT EXISTS \{Schema.NAME}";
    }
    static String useSchema() {
        return STR."USE \{Schema.NAME}";
    }
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

        String getCreateTableQuery() {
            return switch (this) {
                case TEACHER -> getTeacherTableCreationQuery();
                case STUDENT -> getStudentTableCreationQuery();
                case COURSE -> getCourseTableCreationQuery();
                case EXAM -> getExamTableCreationQuery();
                case EXAM_RESULT -> getExamResultTableCreationQuery();
                case ENROLLMENT -> getEnrollmentTableCreationQuery();
            };
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

        private static String getEnrollmentTableCreationQuery() {
            return STR."""
                CREATE TABLE IF NOT EXISTS \{NAME}.\{ENROLLMENT.getTableName()} (
                    student_id INT,
                    course_id INT,
                    has_paid BOOLEAN DEFAULT FALSE,
                    enrollment_status VARCHAR(20)
                    CHECK (enrollment_status IN ('active', 'completed', 'dropped', 'withdrawn')),
                    PRIMARY KEY (student_id, course_id),
                    KEY FK_ENROLLED_STUDENT_ID (student_id),
                    KEY FK_ENROLLED_COURSE_ID (course_id),
                    CONSTRAINT FK_STUDENT_ID FOREIGN KEY (student_id)
                    REFERENCES student (id) ON DELETE CASCADE,
                    CONSTRAINT FK_COURSE_ID FOREIGN KEY (course_id)
                    REFERENCES course (id) ON DELETE CASCADE
                )
                """;
        }

        private static String getExamResultTableCreationQuery() {
            return STR."""
                CREATE TABLE IF NOT EXISTS \{NAME}.\{EXAM_RESULT.getTableName()} (
                exam_id  INT,
                student_id INT,
                grade DOUBLE CHECK (grade >= 0 AND grade <= 100),
                PRIMARY KEY (exam_id,student_id),
                KEY FK_EXAM_RESULT_ID (exam_id),
                KEY FK_EXAM_RESULT_STUDENT_ID (student_id),
                CONSTRAINT FK_EXAM_RESULT_ID FOREIGN KEY (exam_id)
                REFERENCES exam (id) ON DELETE CASCADE,
                CONSTRAINT FK_EXAM_RESULT_STUDENT_ID FOREIGN KEY (student_id)
                REFERENCES student (id) ON DELETE CASCADE
                )
                """;
        }

        private static String getExamTableCreationQuery() {
            return STR."""
                CREATE TABLE IF NOT EXISTS \{NAME}.\{EXAM.getTableName()} (
                 id INT NOT NULL,
                 course_id INT NOT NULL,
                 name VARCHAR(50) NOT NULL,
                 date_time DATETIME NOT NULL,
                 description TEXT,
                 PRIMARY KEY (id),
                 KEY FK_EXAM_COURSE_ID (course_id),
                 CONSTRAINT FK_EXAM_COURSE_ID FOREIGN KEY (course_id)
                 REFERENCES EducationDB.course (id) ON DELETE CASCADE
                 )
                """;
        }

        private static String getCourseTableCreationQuery() {
            return STR."""
                CREATE TABLE IF NOT EXISTS \{NAME}.\{COURSE.getTableName()} (
                    id INT NOT NULL,
                    name VARCHAR(20) NOT NULL,
                    teacher_id INT DEFAULT NULL,
                    PRIMARY KEY(id),
                    KEY FK_TEACHER_ID (teacher_id),
                    CONSTRAINT FK_TEACHER_ID FOREIGN KEY (teacher_id)
                    REFERENCES EducationDB.teacher (id) ON DELETE CASCADE
                )
                """;
        }

        private static String getStudentTableCreationQuery() {
            return STR."""
                CREATE TABLE IF NOT EXISTS \{NAME}.\{STUDENT.getTableName()} (
                    id INT NOT NULL AUTO_INCREMENT,
                    name VARCHAR(50) NOT NULL,
                    email VARCHAR(30) NOT NULL,
                    phone VARCHAR(10) NOT NULL,
                    PRIMARY KEY (id)
                 )
                """;
        }

        private static String getTeacherTableCreationQuery() {
            return STR."""
                CREATE TABLE IF NOT EXISTS \{NAME}.\{TEACHER.getTableName()} (
                    id INT NOT NULL AUTO_INCREMENT,
                    date_of_birth DATE NOT NULL,
                    name VARCHAR(50)  NOT NULL,
                    email VARCHAR(30) NOT NULL,
                    phone VARCHAR(10) NOT NULL,
                    salary DOUBLE NOT NULL,
                    experience INT NOT NULL,
                    major VARCHAR(30) NOT NULL,
                    PRIMARY KEY (id)
                )
                """;
        }

        public record Column(String name, Class<?> type) {
        }
    }
}
