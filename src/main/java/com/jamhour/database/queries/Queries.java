package com.jamhour.database.queries;

import com.jamhour.data.*;
import com.jamhour.database.Database;
import lombok.NoArgsConstructor;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class Queries {

    private static final Database database = Database.getInstance();

    public static <T> Optional<Student> getStudentBy(T thingToGetStudentBy, Student.Column columnToGetStudentBy) {

        if (!database.isConnected()) {
            return Optional.empty();
        }

        final String query = STR."SELECT * FROM \{Student.TABLE_NAME} WHERE \{columnToGetStudentBy.getName()} = ?";
        try (var preparedStatement = database.getConnection().prepareStatement(query)) {

            switch (columnToGetStudentBy) {
                case Student.Column.ID -> preparedStatement.setInt(1, (int) thingToGetStudentBy);
                case Student.Column.NAME,
                        Student.Column.EMAIL,
                        Student.Column.PHONE -> preparedStatement.setString(1, (String) thingToGetStudentBy);
            }

            var resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                return Optional.empty();
            }

            return Optional.of(
                    new Student(
                            resultSet.getString(Student.Column.NAME.getName()),
                            resultSet.getString(Student.Column.EMAIL.getName()),
                            resultSet.getString(Student.Column.PHONE.getName()),
                            resultSet.getInt(Student.Column.ID.getName())
                    )
            );

        } catch (SQLException sqlException) {
            System.err.println(STR."Could not get student from table \{Student.TABLE_NAME} Column: \{columnToGetStudentBy.getName()} using \{thingToGetStudentBy}: \{sqlException.getMessage()}");
            return Optional.empty();
        }
    }

    public static <T> Optional<Teacher> getTeacherBy(T thingToGetTeacherBy, Teacher.Column columnToGetTeacherBy) {

        if (!database.isConnected()) {
            return Optional.empty();
        }

        final String query = STR."SELECT * FROM \{Teacher.TABLE_NAME} WHERE \{columnToGetTeacherBy.getName()} = ?";
        try (var preparedStatement = database.getConnection().prepareStatement(query)) {

            switch (columnToGetTeacherBy) {
                case Teacher.Column.ID,
                        Teacher.Column.EXPERIENCE -> preparedStatement.setInt(1, (int) thingToGetTeacherBy);
                case Teacher.Column.NAME,
                        Teacher.Column.EMAIL,
                        Teacher.Column.PHONE,
                        Teacher.Column.MAJOR -> preparedStatement.setString(1, (String) thingToGetTeacherBy);
                case Teacher.Column.DATE_OF_BIRTH ->
                        preparedStatement.setDate(1, java.sql.Date.valueOf((LocalDate) thingToGetTeacherBy));
                case Teacher.Column.SALARY -> preparedStatement.setDouble(1, (Double) thingToGetTeacherBy);
            }

            var resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                return Optional.empty();
            }

            return Optional.of(
                    new Teacher(
                            resultSet.getString(Teacher.Column.NAME.getName()),
                            resultSet.getString(Teacher.Column.PHONE.getName()),
                            resultSet.getString(Teacher.Column.EMAIL.getName()),
                            resultSet.getString(Teacher.Column.MAJOR.getName()),
                            resultSet.getDouble(Teacher.Column.SALARY.getName()),
                            resultSet.getInt(Teacher.Column.EXPERIENCE.getName()),
                            resultSet.getDate(Teacher.Column.DATE_OF_BIRTH.getName()).toLocalDate(),
                            resultSet.getInt(Teacher.Column.ID.getName())
                    )
            );

        } catch (SQLException sqlException) {
            System.err.println(STR."Could not get student from table \{Teacher.TABLE_NAME} Column: \{columnToGetTeacherBy.getName()} using \{thingToGetTeacherBy}: \{sqlException.getMessage()}");
            return Optional.empty();
        }

    }

    public static <T> Optional<Exam> getExamBy(T thingToGetExamBy, Exam.Column columnToGetExamBy) {

        if (!database.isConnected()) {
            return Optional.empty();
        }

        final String query = STR."SELECT * FROM \{Exam.TABLE_NAME} WHERE \{columnToGetExamBy.getName()} = ?";
        try (var preparedStatement = database.getConnection().prepareStatement(query)) {

            switch (columnToGetExamBy) {
                case Exam.Column.ID,
                        Exam.Column.COURSE_ID -> preparedStatement.setInt(1, (int) thingToGetExamBy);
                case Exam.Column.DESCRIPTION,
                        Exam.Column.NAME -> preparedStatement.setString(1, (String) thingToGetExamBy);
                case Exam.Column.EXAM_DATE_TIME ->
                        preparedStatement.setTimestamp(1, java.sql.Timestamp.valueOf((LocalDateTime) thingToGetExamBy));
            }

            var resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                return Optional.empty();
            }

            return Optional.of(
                    new Exam(
                            resultSet.getString(Exam.Column.ID.getName()),
                            resultSet.getString(Exam.Column.DESCRIPTION.getName()),
                            resultSet.getTimestamp(Exam.Column.EXAM_DATE_TIME.getName()).toLocalDateTime(),
                            resultSet.getInt(Exam.Column.ID.getName()),
                            resultSet.getInt(Exam.Column.COURSE_ID.getName())
                    )
            );

        } catch (SQLException sqlException) {
            System.err.println(STR."Could not get student from table \{Exam.TABLE_NAME} Column: \{columnToGetExamBy.getName()} using \{thingToGetExamBy}: \{sqlException.getMessage()}");
            return Optional.empty();
        }

    }

    public static <T> Optional<Course> getCourseBy(T thingToGetCourseBy, Course.Column columnToGetCourseBy) {

        if (!database.isConnected()) {
            return Optional.empty();
        }

        final String query = STR."SELECT * FROM \{Course.TABLE_NAME} WHERE \{columnToGetCourseBy.getName()} = ?";
        try (var preparedStatement = database.getConnection().prepareStatement(query)) {

            switch (columnToGetCourseBy) {
                case Course.Column.ID,
                        Course.Column.TEACHER_ID -> preparedStatement.setInt(1, (int) thingToGetCourseBy);
                case Course.Column.NAME -> preparedStatement.setString(1, (String) thingToGetCourseBy);
            }

            var resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                return Optional.empty();
            }

            return Optional.of(
                    new Course(
                            resultSet.getString(Course.Column.NAME.getName()),
                            resultSet.getInt(Course.Column.ID.getName()),
                            resultSet.getInt(Course.Column.TEACHER_ID.getName())
                    )
            );

        } catch (SQLException sqlException) {
            System.err.println(STR."Could not get student from table \{Course.TABLE_NAME} Column: \{columnToGetCourseBy.getName()} using \{thingToGetCourseBy}: \{sqlException.getMessage()}");
            return Optional.empty();
        }

    }

    public static <T> Optional<ExamResult> getExamResultBy(T thingToGetCourseBy, ExamResult.Column columnToGetExamResultBy) {

        if (!database.isConnected()) {
            return Optional.empty();
        }

        final String query = STR."SELECT * FROM \{ExamResult.TABLE_NAME} WHERE \{columnToGetExamResultBy.getName()} = ?";
        try (var preparedStatement = database.getConnection().prepareStatement(query)) {

            switch (columnToGetExamResultBy) {
                case ExamResult.Column.STUDENT_ID,
                        ExamResult.Column.EXAM_ID -> preparedStatement.setInt(1, (int) thingToGetCourseBy);
                case ExamResult.Column.GRADE -> preparedStatement.setDouble(1, (double) thingToGetCourseBy);
            }

            var resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                return Optional.empty();
            }

            return Optional.of(
                    new ExamResult(
                            resultSet.getDouble(ExamResult.Column.GRADE.getName()),
                            resultSet.getInt(ExamResult.Column.EXAM_ID.getName()),
                            resultSet.getInt(ExamResult.Column.STUDENT_ID.getName())
                    )
            );

        } catch (SQLException sqlException) {
            System.err.println(STR."Could not get student from table \{ExamResult.TABLE_NAME} Column: \{columnToGetExamResultBy.getName()} using \{thingToGetCourseBy}: \{sqlException.getMessage()}");
            return Optional.empty();
        }

    }

    public static <T> Optional<Enrollment> getEnrollmentBy(T thingToGetCourseBy, Enrollment.Column columnToGetEnrollmentBy) {

        if (!database.isConnected()) {
            return Optional.empty();
        }

        final String query = STR."SELECT * FROM \{Enrollment.TABLE_NAME} WHERE \{columnToGetEnrollmentBy.getName()} = ?";
        try (var preparedStatement = database.getConnection().prepareStatement(query)) {

            switch (columnToGetEnrollmentBy) {
                case Enrollment.Column.STUDENT_ID, Enrollment.Column.COURSE_ID ->
                        preparedStatement.setInt(1, (int) thingToGetCourseBy);
                case Enrollment.Column.PAID -> preparedStatement.setBoolean(1, (boolean) thingToGetCourseBy);
                case Enrollment.Column.STATUS ->
                        preparedStatement.setString(1, ((Enrollment.EnrollmentStatus) thingToGetCourseBy).getStatus());
            }

            var resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                return Optional.empty();
            }

            return Optional.of(
                    new Enrollment(
                            Enrollment.EnrollmentStatus.valueOf(resultSet.getString(Enrollment.Column.STATUS.getName()).toUpperCase()),
                            resultSet.getBoolean(Enrollment.Column.PAID.getName()),
                            resultSet.getInt(Enrollment.Column.COURSE_ID.getName()),
                            resultSet.getInt(Enrollment.Column.STUDENT_ID.getName())
                    )
            );

        } catch (SQLException sqlException) {
            System.err.println(STR."Could not get student from table \{Enrollment.TABLE_NAME} Column: \{columnToGetEnrollmentBy.getName()} using \{thingToGetCourseBy}: \{sqlException.getMessage()}");
            return Optional.empty();
        }

    }

}
