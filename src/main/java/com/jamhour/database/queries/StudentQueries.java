package com.jamhour.database.queries;

import com.jamhour.data.Student;
import com.jamhour.database.Database;
import lombok.NoArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class StudentQueries {

    public static Optional<Student> getStudentById(int id) {
        var database = Database.getInstance();

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

}
