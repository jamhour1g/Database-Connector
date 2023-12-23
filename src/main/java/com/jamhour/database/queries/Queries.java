package com.jamhour.database.queries;

import com.jamhour.database.Database;
import com.jamhour.database.Schema;
import com.jamhour.database.Table;
import com.jamhour.database.TableColumn;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Queries {

    private static final Database database = Database.getInstance();
    private static final String DATABASE_NOT_CONNECTED_MESSAGE = STR."Database \{Schema.NAME} is not connected";

    public static <T, R>
    Optional<R> getFromTableUsing(Table table, TableColumn column, T value) {

        if (!database.isConnected()) {
            throw new IllegalStateException(DATABASE_NOT_CONNECTED_MESSAGE);
        }

        final boolean isColumnInTable = table.getTableColumns().containsKey(column);

        if (!isColumnInTable) {
            throw new IllegalArgumentException(STR."Column \{column.columnName()} does not exist on table \{table.getTableName()}");
        }

        if (column.getType() != value.getClass()) {
            throw new IllegalArgumentException(STR."Column \{column.columnName()} is of type \{column.getType()} but value is of type \{value.getClass()}");
        }

        final String query = STR."SELECT * FROM \{table.getTableName()} WHERE \{column.columnName()} = ?";
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

    public static <T> List<T> getAllInTable(Table table) {

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
