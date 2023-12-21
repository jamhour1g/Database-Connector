package com.jamhour.database.queries;

import com.jamhour.data.Student;
import com.jamhour.database.Database;
import lombok.NoArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class StudentQueries {

    private static final Database database = Database.getInstance();

    public static <T> Optional<Student> getStudentBy(T thingToGetStudentBy, Student.Column columnToGetStudentBy) {

        if (!database.isConnected()) {
            return Optional.empty();
        }

        final String query = STR."SELECT * FROM \{Student.TABLE_NAME} WHERE \{columnToGetStudentBy.getName()} = ?";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(query)) {

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

}
