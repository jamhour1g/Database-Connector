package com.jamhour.database;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Database implements AutoCloseable {
    private static Database instance;

    private final MysqlDataSource dataSource = new MysqlDataSource();

    private Connection connection;
    private Statement statement;

    private Database() {
        setUpDataSource();
        connect();
        setUpSchema();
    }

    private void setUpSchema() {
        try {
            String databaseName = "EducationDB";
            statement.execute("CREATE DATABASE IF NOT EXISTS " + databaseName);
            statement.execute("USE " + databaseName);
            createTables();
        } catch (SQLException e) {
            System.err.println("Failed to create schema. Error: " + e);
        }
    }

    private void createTables() {
        String createTeacherTable = """
                CREATE TABLE IF NOT EXISTS EducationDB.teacher (
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

        String createStudentTable = """
                CREATE TABLE IF NOT EXISTS EducationDB.student (
                    id INT NOT NULL AUTO_INCREMENT,
                    name VARCHAR(50) NOT NULL,
                    email VARCHAR(30) NOT NULL,
                    phone VARCHAR(10) NOT NULL,
                    PRIMARY KEY (id)
                 )
                """;

        String createCourseTable = """
                CREATE TABLE IF NOT EXISTS EducationDB.course (
                    id INT NOT NULL,
                    name VARCHAR(20) NOT NULL,
                    teacher_id INT DEFAULT NULL,
                    PRIMARY KEY(id),
                    KEY FK_TEACHER_ID (teacher_id),
                    CONSTRAINT FK_TEACHER_ID FOREIGN KEY (teacher_id)
                    REFERENCES EducationDB.teacher (id) ON DELETE CASCADE
                )
                """;

        String createExamTable = """
                CREATE TABLE IF NOT EXISTS EducationDB.exam (
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

        String createExamResultsTable = """
                CREATE TABLE IF NOT EXISTS EducationDB.exam_result (
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
        String createEnrollmentTable = """
                CREATE TABLE IF NOT EXISTS EducationDB.enrollment (
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

        try {
            statement.execute(createTeacherTable);
            statement.execute(createStudentTable);
            statement.execute(createCourseTable);
            statement.execute(createExamTable);
            statement.execute(createExamResultsTable);
            statement.execute(createEnrollmentTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void setUpDataSource() {
        dataSource.setServerName("localhost");
        dataSource.setPort(3306);
        dataSource.setUser(System.getenv("MYSQL_DEVUSER"));
        dataSource.setPassword(System.getenv("MYSQL_DEVUSER_PASSWORD"));
    }

    private void connect() {
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
        } catch (SQLException e) {
            System.err.println("Failed to connect to database. Error: " + e);
        }
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    @Override
    public void close() throws Exception {
        if (statement != null) {
            statement.close();
        }
        if (connection != null) {
            connection.close();
        }

    }
}