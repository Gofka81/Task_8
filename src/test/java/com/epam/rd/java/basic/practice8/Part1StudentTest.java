package com.epam.rd.java.basic.practice8;

import com.epam.rd.java.basic.practice8.db.DBManager;
import com.epam.rd.java.basic.practice8.db.constant.Constant;
import com.epam.rd.java.basic.practice8.db.entity.Team;
import com.epam.rd.java.basic.practice8.db.entity.User;
import org.junit.*;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Part1StudentTest {
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:~/test";
    private static final String URL_CONNECTION = "jdbc:h2:~/test;user=youruser;password=yourpassword;";
    private static final String USER = "youruser";
    private static final String PASS = "yourpassword";

    private static DBManager dbManager;
    private static Connection connection;

    @BeforeClass
    public static void beforeTest(){

        try (OutputStream output = new FileOutputStream("app.properties")) {
            Properties prop = new Properties();
            prop.setProperty("connection.url", URL_CONNECTION);
            prop.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        }

        dbManager = DBManager.getInstance();

        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement statement = con.createStatement()) {
            connection = con;
            String sql = "CREATE TABLE IF NOT EXISTS users (\n" +
                    "  id INTEGER(11) NOT NULL AUTO_INCREMENT,\n" +
                    " login VARCHAR(20) NOT NULL, \n" +
                    "  PRIMARY KEY (id));";

            statement.executeUpdate(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    @Test
    public void mustInsertUsersToDB(){
        User user1 = new User(1,"ivanov");
        User user2 = null;
        dbManager.insertUser(User.createUser("ivanov"));
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT login FROM users ORDER BY id");
            if (rs.next()){
                user2 =new User(rs.getString("login"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Assert.assertEquals(user1,user2);
    }
}