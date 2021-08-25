package com.epam.rd.java.basic.practice8;

import com.epam.rd.java.basic.practice8.db.DBManager;
import com.epam.rd.java.basic.practice8.db.entity.Team;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.util.Properties;

public class Part2StudentTest {
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
                    "  PRIMARY KEY (id));";

            statement.executeUpdate(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void mustInsertTeamsToDB(){
        Team team1 = new Team(1,"teamA");
        Team team2 = null;
        dbManager.insertTeam(Team.createTeam("teamA"));
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT id,name FROM teams ORDER BY id");
            if (rs.next()){
                team2 =new Team(rs.getInt("id"),rs.getString("name"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Assert.assertTrue(team1.getName().equals(team2.getName())&& team1.getId()== team2.getId());
    }

    @Test
    public void toStringTeam(){
        Team team = new Team(2,"teamA");
        Assert.assertEquals("teamA",team.toString());
    }

    @AfterClass
    public static void tearDownDB(){
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement statement = con.createStatement()) {
            connection = con;
            String sql = "DROP TABLE teams";

            statement.executeUpdate(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}