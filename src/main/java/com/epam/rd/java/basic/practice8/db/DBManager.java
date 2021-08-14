package com.epam.rd.java.basic.practice8.db;
import java.sql.Connection;
import java.sql.SQLException;

public class DBManager {

    private static DBManager dbManager;

    private DBManager() {
    }

    public static DBManager getInstance() {
        return null;
    }

    public Connection getConnection(String connectionUrl) throws SQLException {
        return null;
    }

}
