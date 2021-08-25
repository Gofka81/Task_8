package com.epam.rd.java.basic.practice8;

import com.epam.rd.java.basic.practice8.db.DBManager;
import com.epam.rd.java.basic.practice8.db.entity.Team;
import com.epam.rd.java.basic.practice8.db.entity.User;
import org.junit.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Part3StudentTest {
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:~/test";
    private static final String URL_CONNECTION = "jdbc:h2:~/test;user=youruser;password=yourpassword;";
    private static final String USER = "youruser";
    private static final String PASS = "yourpassword";

    private static DBManager dbManager;
    private static Connection connection;

    @Before
    public void beforeTest(){

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

        dbManager.insertUser(User.createUser("ivanov"));
        dbManager.insertUser(User.createUser("teamA"));
        dbManager.insertUser(User.createUser("teamB"));
    }

    @After
    public void tearDownDB(){
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
    public void shoudGetUserClassWithId(){
        User user1 = new User("ivanov");
        user1.setId(1);
        User user2 = null;
        user2 = dbManager.getUser("ivanov");
        Assert.assertTrue(user1.getLogin().equals(user2.getLogin())&& user1.getId()== user2.getId());
    }

    @Test
    public void shoudGetTeamClassWithId(){
        Team team1 = new Team();
        team1.setId(1);
        team1.setName("ivanov");
        Team team2 = null;
        team2 = dbManager.getTeam("teamA");
        Assert.assertTrue(team1.getName().equals(team2.getName())&& team1.getId()== team2.getId());
    }

    @Test
    public void shoudSetTeamsForUser(){
        User user = dbManager.getUser("ivanov");
        Team team1 = dbManager.getTeam("teamA");
        Team team2 = dbManager.getTeam("teamB");
        dbManager.setTeamsForUser(user,team1,team2);
        List<Team> teams1 = new ArrayList<>();
        teams1.add(team1);
        teams1.add(team2);
        List<Team> teams2 = dbManager.getUserTeams(user);
        boolean checkEquals = true;
        for(int i =0; i< teams1.size(); i++){
            if(!teams1.get(i).getName().equals(teams2.get(i).getName())){
                checkEquals = false;
            }
        }
        Assert.assertTrue(checkEquals);
    }

    @Test
    public void shoudPrintCorrectCountOfusers(){
        User user1 = dbManager.getUser("ivanov");
        List<User> users =dbManager.findAllUsers();
        User user2 = users.get(0);
        Assert.assertTrue(users.size() == 1 && user1.getLogin().equals(user2.getLogin()));
    }

}