package com.jamhour.database;

import com.jamhour.data.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    public static Set<String> getTableNames() {
        return Arrays.stream(Tables.values())
                .map(Tables::getTableName)
                .collect(Collectors.toSet());
    }

    @Getter
    public enum Tables implements Table {
        TEACHER("teacher"),
        STUDENT("student"),
        COURSE("course"),
        ENROLLMENT("enrollment"),
        EXAM("exam"),
        EXAM_RESULT("exam_result");

        private final String tableName;

        Tables(String tableName) {
            this.tableName = tableName;
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

        private static String getEnrollmentTableCreationQuery() {
            return STR."""
            CREATE TABLE IF NOT EXISTS \{NAME}.\{ENROLLMENT.getTableName()} (
                student_id INT,
                course_id INT,
                paid BOOLEAN DEFAULT FALSE,
                enrollment_status VARCHAR(20)
                CHECK (enrollment_status IN ('active', 'completed', 'dropped', 'withdrawn')),
                PRIMARY KEY (student_id, course_id),
                KEY FK_ENROLLED_STUDENT_ID (student_id),
                KEY FK_ENROLLED_COURSE_ID (course_id),
                CONSTRAINT FK_STUDENT_ID FOREIGN KEY (student_id)
                REFERENCES student (id)
                ON DELETE CASCADE
                ON UPDATE CASCADE,
                CONSTRAINT FK_COURSE_ID FOREIGN KEY (course_id)
                REFERENCES course (id)
                ON DELETE CASCADE
                ON UPDATE CASCADE
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
                REFERENCES exam (id)
                ON DELETE CASCADE
                ON UPDATE CASCADE,
                CONSTRAINT FK_EXAM_RESULT_STUDENT_ID FOREIGN KEY (student_id)
                REFERENCES student (id)
                ON DELETE CASCADE
                ON UPDATE CASCADE
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
                REFERENCES EducationDB.course (id)
                ON DELETE CASCADE
                ON UPDATE CASCADE
            )
            """;
        }

        private static String getCourseTableCreationQuery() {
            return STR."""
            CREATE TABLE IF NOT EXISTS \{NAME}.\{COURSE.getTableName()} (
                id INT NOT NULL AUTO_INCREMENT,
                name TEXT NOT NULL,
                teacher_id INT DEFAULT NULL,
                PRIMARY KEY(id),
                KEY FK_TEACHER_ID (teacher_id),
                CONSTRAINT FK_TEACHER_ID FOREIGN KEY (teacher_id)
                REFERENCES EducationDB.teacher (id)
                ON DELETE CASCADE
                ON UPDATE CASCADE
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


        public Map<TableColumn, String> getTableColumns() {
            return switch (this) {
                case TEACHER -> Teacher.getTableColumns();
                case STUDENT -> Student.getTableColumns();
                case COURSE -> Course.getTableColumns();
                case EXAM -> Exam.getTableColumns();
                case EXAM_RESULT -> ExamResult.getTableColumns();
                case ENROLLMENT -> Enrollment.getTableColumns();
            };
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T getObject(ResultSet resultSet) throws SQLException {
            return switch (this) {
                case TEACHER -> (T) Teacher.get(resultSet);
                case STUDENT -> (T) Student.get(resultSet);
                case COURSE -> (T) Course.get(resultSet);
                case EXAM -> (T) Exam.get(resultSet);
                case EXAM_RESULT -> (T) ExamResult.get(resultSet);
                case ENROLLMENT -> (T) Enrollment.get(resultSet);
            };
        }

        @Override
        public PreparedStatement setColumnDetails(PreparedStatement preparedStatement,
                                                  TableColumn column,
                                                  Object thingToSet) throws SQLException {
            return switch (this) {
                case TEACHER -> Teacher.Column.setColumnDetails(preparedStatement, (Teacher.Column) column, thingToSet);
                case STUDENT -> Student.Column.setColumnDetails(preparedStatement, (Student.Column) column, thingToSet);
                case COURSE -> Course.Column.setColumnDetails(preparedStatement, (Course.Column) column, thingToSet);
                case EXAM -> Exam.Column.setColumnDetails(preparedStatement, (Exam.Column) column, thingToSet);
                case EXAM_RESULT ->
                        ExamResult.Column.setColumnDetails(preparedStatement, (ExamResult.Column) column, thingToSet);
                case ENROLLMENT ->
                        Enrollment.Column.setColumnDetails(preparedStatement, (Enrollment.Column) column, thingToSet);
            };
        }

        @Override
        public PreparedStatement setColumnDetails(PreparedStatement preparedStatement,
                                                  TableColumn columnToSet,
                                                  Object valueToSet,
                                                  TableColumn columnToCompare,
                                                  Object valueToCompare) throws SQLException {

            return switch (this) {
                case TEACHER -> Teacher.Column.setColumnDetails(preparedStatement,
                        (Teacher.Column) columnToSet,
                        valueToSet,
                        (Teacher.Column) columnToCompare,
                        valueToCompare);
                case STUDENT -> Student.Column.setColumnDetails(preparedStatement,
                        (Student.Column) columnToSet,
                        valueToSet,
                        (Student.Column) columnToCompare,
                        valueToCompare);
                case COURSE -> Course.Column.setColumnDetails(preparedStatement,
                        (Course.Column) columnToSet,
                        valueToSet,
                        (Course.Column) columnToCompare,
                        valueToCompare);
                case EXAM -> Exam.Column.setColumnDetails(preparedStatement,
                        (Exam.Column) columnToSet,
                        valueToSet,
                        (Exam.Column) columnToCompare,
                        valueToCompare);
                case EXAM_RESULT -> ExamResult.Column.setColumnDetails(preparedStatement,
                        (ExamResult.Column) columnToSet,
                        valueToSet,
                        (ExamResult.Column) columnToCompare,
                        valueToCompare);
                case ENROLLMENT -> Enrollment.Column.setColumnDetails(preparedStatement,
                        (Enrollment.Column) columnToSet,
                        valueToSet,
                        (Enrollment.Column) columnToCompare,
                        valueToCompare);
            };
        }

        @Override
        public String getColumns() {
            return switch (this) {
                case TEACHER -> Teacher.getInsertColumns();
                case STUDENT -> Student.getInsertColumns();
                case COURSE -> Course.getInsertColumns();
                case EXAM -> Exam.getInsertColumns();
                case EXAM_RESULT -> ExamResult.getInsertColumns();
                case ENROLLMENT -> Enrollment.getInsertColumns();
            };
        }

        @Override
        public String getValues(Object thingToInsert) {
            return switch (this) {
                case TEACHER -> Teacher.getInsertValues((Teacher) thingToInsert);
                case STUDENT -> Student.getInsertValues((Student) thingToInsert);
                case COURSE -> Course.getInsertValues((Course) thingToInsert);
                case EXAM -> Exam.getInsertValues((Exam) thingToInsert);
                case EXAM_RESULT -> ExamResult.getInsertValues((ExamResult) thingToInsert);
                case ENROLLMENT -> Enrollment.getInsertValues((Enrollment) thingToInsert);
            };
        }

    }

}
