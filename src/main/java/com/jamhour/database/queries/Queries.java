package com.jamhour.database.queries;

import com.jamhour.data.Student;
import com.jamhour.data.Teacher;
import com.jamhour.database.Database;
import lombok.NoArgsConstructor;

import java.sql.SQLException;
import java.time.LocalDate;
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
            System.err.println(STR."Could not get student from table \{columnToGetStudentBy.getName()} using \{thingToGetStudentBy}: \{sqlException.getMessage()}");
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
            System.err.println(STR."Could not get student from table \{columnToGetTeacherBy.getName()} using \{thingToGetTeacherBy}: \{sqlException.getMessage()}");
            return Optional.empty();
        }

    }

}
