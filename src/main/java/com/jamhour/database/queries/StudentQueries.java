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

    public static Optional<Student> getStudentById(int id) {

        if (!database.isConnected()) {
            return Optional.empty();
        }

        final String query = "SELECT * FROM students WHERE id = ?";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(query)) {

            preparedStatement.setInt(1, id);

            var resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                return Optional.empty();
            }
            return Optional.of(
                    new Student(
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("phone"),
                            resultSet.getInt("id")
                    )
            );

        } catch (SQLException sqlException) {
            return Optional.empty();
        }
    }

    public static Optional<Student> getStudentByName(String name) {
        if (!database.isConnected()) {
            return Optional.empty();
        }

        final String query = "SELECT * FROM students WHERE name = '?'";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(query)) {

            preparedStatement.setString(1, name);

            var resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                return Optional.empty();
            }

            return Optional.of(
                    new Student(
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("phone"),
                            resultSet.getInt("id")
                    )
            );

        } catch (SQLException sqlException) {
            return Optional.empty();
        }
    }

    public static Optional<Student> getStudentByEmail(String email) {

        if (!database.isConnected()) {
            return Optional.empty();
        }

        final String query = "SELECT * FROM students WHERE email = '?'";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(query)) {

            preparedStatement.setString(1, email);

            var resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                return Optional.empty();
            }

            return Optional.of(
                    new Student(
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("phone"),
                            resultSet.getInt("id")
                    )
            );

        } catch (SQLException sqlException) {
            return Optional.empty();
        }
    }


}
