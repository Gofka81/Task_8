package com.epam.rd.java.basic.practice8;

import com.epam.rd.java.basic.practice8.db.DBManager;
import com.epam.rd.java.basic.practice8.db.entity.Team;
import com.epam.rd.java.basic.practice8.db.entity.User;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Part4StudentTest {
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
            String sql = "CREATE TABLE IF NOT EXISTS teams (\n" +
                    "  id INTEGER(11) NOT NULL AUTO_INCREMENT,\n" +
                    " name VARCHAR(20) NOT NULL, \n" +
                    "  PRIMARY KEY (id));" +
                    "\nCREATE TABLE IF NOT EXISTS users (\n"+
                    " id INTEGER(11) NOT NULL AUTO_INCREMENT,\n"+
                    " login VARCHAR(20) NOT NULL, \n "+
                    " PRIMARY KEY (id));\n" +
                    "CREATE TABLE users_teams (\n" +
                    "user_id INT REFERENCES users(id) ON DELETE CASCADE,\n" +
                    "team_id INT REFERENCES teams(id) ON DELETE CASCADE,\n" +
                    "UNIQUE (user_id, team_id));";

            statement.executeUpdate(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDownDB(){
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement statement = con.createStatement()) {
            connection = con;
            String sql = "DROP TABLE IF EXISTS teams; \n" +
                    "DROP TABLE IF EXISTS users; \n" +
                    "DROP TABLE IF EXISTS users_teams;";

            statement.executeUpdate(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void shoudDeleteTeam(){
        dbManager.insertUser(User.createUser("ivanov"));
        dbManager.insertUser(User.createUser("teamA"));
        dbManager.insertUser(User.createUser("teamB"));

        User user = dbManager.getUser("ivanov");
        Team team1 = dbManager.getTeam("teamA");
        Team team2 = dbManager.getTeam("teamB");
        dbManager.setTeamsForUser(user,team1,team2);
        dbManager.deleteTeam(team2);
        List<Team> teams1 = dbManager.findAllTeams();
        Team team = teams1.get(0);
        Assert.assertEquals(team.getName(), team1.getName());
    }
}