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
    private static final String DATABASE_NOT_CONNECTED_MESSAGE = STR."Database \{Schema.NAME} is not connected. " +
                                                                 "To connect, call Database.connect() " +
                                                                 "or check if the database connection is closed.";

    public static <T> Optional<T> getFromTableUsing(Table table, TableColumn column, Object thingToCompare) {

        checkConnection();
        checkTableExists(table);
        ensureColumnExistsInTable(table, column);
        ensureTypeMatch(column, thingToCompare);

        final String query = STR."SELECT * FROM \{Schema.NAME}.\{table.getTableName()} WHERE \{column.columnName()} = ?";
        try (var preparedStatement = database.getConnection().prepareStatement(query)) {

            ResultSet resultSet = table.setColumnDetails(preparedStatement, column, thingToCompare).executeQuery();

            if (!resultSet.next()) {
                return Optional.empty();
            }

            return Optional.of(table.getObject(resultSet));

        } catch (SQLException sqlException) {
            return Optional.empty();
        }

    }

    public static <T> List<T> getAllInTable(Table table) {

        checkConnection();
        checkTableExists(table);

        final String query = STR."SELECT * FROM \{Schema.NAME}.\{table.getTableName()}";

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

    public static int insertIntoTable(Table table, Object thingToInsert) {

        checkConnection();
        checkTableExists(table);

        final String insertQuery = STR."INSERT INTO \{Schema.NAME}.\{table.getTableName()} (\{table.getColumns()}) VALUES (\{table.getValues(thingToInsert)})";

        try {
            return database.getStatement().executeUpdate(insertQuery);
        } catch (SQLException e) {
            return 0;
        }

    }

    public static int deleteFromTableUsing(Table table, TableColumn column, Object thingToCompare) {

        checkConnection();
        checkTableExists(table);
        ensureColumnExistsInTable(table, column);
        ensureTypeMatch(column, thingToCompare);

        final String deleteQuery = STR."DELETE FROM \{Schema.NAME}.\{table.getTableName()} WHERE \{column.columnName()} = ?";

        try (var preparedStatement = database.getConnection().prepareStatement(deleteQuery)) {
            table.setColumnDetails(preparedStatement, column, thingToCompare).executeUpdate();
            return preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            return 0;
        }

    }

    public static int updateTableUsing(Table table,
                                       TableColumn columnToSet,
                                       Object valueToSet,
                                       TableColumn columnToCompare,
                                       Object valueToCompare) {

        checkConnection();
        checkTableExists(table);
        ensureTypeMatch(columnToSet, valueToSet);
        ensureTypeMatch(columnToCompare, valueToCompare);
        ensureColumnExistsInTable(table, columnToSet);
        ensureColumnExistsInTable(table, columnToCompare);

        final String updateQuery = STR."""
        UPDATE \{Schema.NAME}.\{table.getTableName()}
        SET \{table.getTableName()}.\{columnToSet.columnName()} = ?
        WHERE \{table.getTableName()}.\{columnToCompare.columnName()} = ?
        """;

        try (var preparedStatement = database.getConnection().prepareStatement(updateQuery)) {

            table.setColumnDetails(preparedStatement, columnToSet, valueToSet, columnToCompare, valueToCompare).executeUpdate();

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            return 0;
        }

    }

    private static void ensureColumnExistsInTable(Table table, TableColumn column) {

        if (!table.getTableColumns().containsKey(column)) {
            throw new IllegalArgumentException(
                    "Column %s does not exist on table %s"
                            .formatted(
                                    column.columnName(),
                                    table.getTableName()
                            )
            );
        }

    }

    private static void ensureTypeMatch(TableColumn column, Object thingToCompare) {
        if (column.getType() != thingToCompare.getClass()) {
            throw new IllegalArgumentException(
                    "Column %s is of type %s but value is of type %s"
                            .formatted(
                                    column.columnName(),
                                    column.getType(),
                                    thingToCompare.getClass()
                            )
            );
        }
    }

    private static void checkTableExists(Table table) {

        if (!Schema.getTableNames().contains(table.getTableName())) {
            throw new IllegalArgumentException(
                    "Table %s does not exist in database %s"
                            .formatted(
                                    table.getTableName(),
                                    Schema.NAME
                            )
            );
        }

    }

    private static void checkConnection() {
        if (!database.isConnected()) {
            throw new IllegalStateException(DATABASE_NOT_CONNECTED_MESSAGE);
        }
    }

}
