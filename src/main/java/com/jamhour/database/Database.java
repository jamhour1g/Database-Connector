package com.jamhour.database;

import com.mysql.cj.jdbc.MysqlDataSource;
import lombok.Getter;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Getter
public class Database implements AutoCloseable {
    private static Database instance;

    private final MysqlDataSource dataSource = new MysqlDataSource();

    private Connection connection;
    private Statement statement;

    private boolean connected;

    private Database() {
        setUpDataSource();
        connect();
        setUpSchema();
    }

    private void setUpSchema() {
        try {
            statement.execute(Schema.getSchemaCreationQuery());
            statement.execute(Schema.useSchema());
            createTables();
        } catch (SQLException e) {
            System.err.println(STR."Failed to create schema. Error: \{e}");
        }
    }

    private void createTables() {

        try {
            for (Schema.Tables table : Schema.Tables.values()) {
                statement.execute(table.getCreateTableQuery());
            }
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
            connected = true;
        } catch (SQLException e) {
            System.err.println(STR."Failed to connect to database. Error: \{e}");
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