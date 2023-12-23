package com.jamhour.database.queries;

import com.jamhour.database.Database;
import com.jamhour.database.Schema;
import com.jamhour.database.TableColumn;
import lombok.experimental.UtilityClass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class Queries {

    private final Database database = Database.getInstance();
    public final String DATABASE_NOT_CONNECTED_MESSAGE = STR."Database \{Schema.NAME} is not connected";

    public <T, S extends Enum<? extends TableColumn> & TableColumn, R>
    Optional<R> getFromTableUsing(Schema.Tables table, S column, T value) {

        if (!database.isConnected()) {
            throw new IllegalStateException(DATABASE_NOT_CONNECTED_MESSAGE);
        }

        String columnName = table.getTableColumns().get(column);

        if (columnName == null) {
            throw new IllegalArgumentException(STR."Column \{column.columnName()} does not exist on table \{table.getTableName()}");
        }

        if (column.getType() != value.getClass()) {
            throw new IllegalArgumentException(STR."Column \{column.columnName()} is of type \{column.getType()} but value is of type \{value.getClass()}");
        }

        final String query = STR."SELECT * FROM \{table.getTableName()} WHERE \{columnName} = ?";
        try (var preparedStatement = database.getConnection().prepareStatement(query)) {

            ResultSet resultSet = table.setColumnDetails(preparedStatement, column, value).executeQuery();

            if (!resultSet.next()) {
                return Optional.empty();
            }

            return Optional.of(table.getObject(resultSet));

        } catch (SQLException sqlException) {
            return Optional.empty();
        }

    }

    public <T> List<T> getAllInTable(Schema.Tables table) {

        if (!database.isConnected()) {
            throw new IllegalStateException(DATABASE_NOT_CONNECTED_MESSAGE);
        }

        final String query = STR."SELECT * FROM \{table.getTableName()}";
        try (var resultSet = database.getStatement().executeQuery(query)) {

            List<T> list = new ArrayList<>();

            while (resultSet.next()) {
                list.add(table.getObject(resultSet));
            }

            return list;
        } catch (SQLException e) {
            return List.of();
        }

    }

}
