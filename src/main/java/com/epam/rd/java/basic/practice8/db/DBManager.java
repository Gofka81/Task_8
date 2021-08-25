package com.epam.rd.java.basic.practice8.db;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.epam.rd.java.basic.practice8.db.entity.Team;
import com.epam.rd.java.basic.practice8.db.entity.User;

public class DBManager {

    private static DBManager dbManager;
    private Connection con;
    private static final Logger logger = Logger.getAnonymousLogger();

    private DBManager() {
        try {
            con = DriverManager.getConnection(getURL());
        } catch (SQLException e) {
            logger.log(Level.SEVERE,e.getMessage());
        }
    }

    private String getURL() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("app.properties")){

            properties.load(fis);
        } catch (IOException e) {
            logger.log(Level.SEVERE,e.getMessage());
        }
        return properties.getProperty("connection.url");
    }

    public static DBManager getInstance() {
        if(dbManager == null){
            dbManager = new DBManager();
        }
        return dbManager;
    }

    public void insertUser(User user)  {
        ResultSet rs = null;
        try(PreparedStatement pst = con.prepareStatement("INSERT INTO  users(login) VALUES(?)", Statement.RETURN_GENERATED_KEYS)){
            pst.setString(1, user.getLogin());
            pst.execute();
            rs = pst.getGeneratedKeys();
            if(rs.next()){
                int id = rs.getInt(1);
                user.setId(id);
            }
        } catch (SQLException | NullPointerException e) {
            logger.log(Level.SEVERE,e.getMessage());
        }
        finally {
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.log(Level.SEVERE,e.getMessage());
                }
            }
        }
    }

    public List<User> findAllUsers(){
        List<User> users = new ArrayList<>();
        ResultSet rs = null;
        try (Statement st = con.createStatement()){
            rs = st.executeQuery("SELECT login FROM users ORDER BY id");

            while (rs.next()){
                users.add(new User(rs.getString("login")));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE,e.getMessage());
        }finally {
            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.log(Level.SEVERE,e.getMessage());
                }
            }
        }
        return users;
    }

    public void insertTeam(Team team) {
        ResultSet rst = null;
        try(PreparedStatement pst =con.prepareStatement("INSERT INTO  teams(name) VALUES(?)",Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, team.getName());
            pst.execute();
            rst = pst.getGeneratedKeys();
            if(rst.next()){
                int id = rst.getInt(1);
                team.setId(id);
            }
        } catch (SQLException | NullPointerException e) {
            logger.log(Level.SEVERE,e.getMessage());
        }
        finally {
            try {
                if (rst != null) {
                    rst.close();
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE,e.getMessage());
            }
        }
    }

    public List<Team> findAllTeams() {
        List<Team> teams = new ArrayList<>();
        ResultSet rs = null;
        try(Statement st =con.createStatement()) {
            rs = st.executeQuery("SELECT name FROM teams ORDER BY id");

            while (rs.next()){
                teams.add(new Team(rs.getString("name")));
            }
        } catch (SQLException | NullPointerException e) {
            logger.log(Level.SEVERE,e.getMessage());
        }
        finally {
            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.log(Level.SEVERE,e.getMessage());
                }
            }
        }
        return teams;
    }

    public Team getTeam(String name) {
        ResultSet rs = null;
        try (PreparedStatement pst= con.prepareStatement("SELECT id,name FROM teams WHERE name = ?")){
            pst.setString(1,name);
            rs = pst.executeQuery();
            if(rs.next())
                return new Team(rs.getInt("id"),rs.getString("name"));
        } catch (SQLException | NullPointerException e) {
            logger.log(Level.SEVERE,e.getMessage());
        }
        finally {
            try {
                if(rs != null)
                    rs.close();
            } catch (SQLException e) {
                logger.log(Level.SEVERE,e.getMessage());
            }
        }
        return null;
    }

    public User getUser(String login)  {
        ResultSet rs = null;
        try(PreparedStatement pst = con.prepareStatement("SELECT id,login FROM users WHERE login = ?")){
            pst.setString(1,login);
            rs = pst.executeQuery();
            if(rs.next())
                return new User(rs.getInt("id"),rs.getString("login"));
        } catch (SQLException | NullPointerException e) {
            logger.log(Level.SEVERE,e.getMessage());
        }
        finally {
            try {
                if(rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE,e.getMessage());
            }
        }
        return null;
    }

    public Connection getConnection(String connectionUrl){
        try {
            con = DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            logger.log(Level.SEVERE,e.getMessage());
        }
        return con;
    }

    public void setTeamsForUser(User user, Team... teams){
        if(user != null) {
            int id = user.getId();
            for (Team team : teams) {
                try (PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO users_teams(team_id,user_id) VALUES(?,?)")) {
                    con.setAutoCommit(false);
                    preparedStatement.setInt(1, team.getId());
                    preparedStatement.setInt(2, id);
                    con.commit();
                    preparedStatement.execute();
                } catch (SQLException | NullPointerException e) {
                    try {
                        con.rollback();
                    } catch (SQLException ex) {
                        logger.log(Level.SEVERE,ex.getMessage());
                    }
                } finally {
                    try {
                        con.setAutoCommit(true);
                    } catch (SQLException ex) {
                        logger.log(Level.SEVERE,ex.getMessage());
                    }
                }
            }
        }
    }

    public List<Team> getUserTeams(User user){
        List<Team> teams = new ArrayList<>();
        ResultSet rs = null;
        try(PreparedStatement pst = con.prepareStatement("SELECT teams.id, teams.name FROM users_teams" +
                " INNER JOIN teams ON users_teams.team_id = teams.id" +
                " INNER JOIN users ON users_teams.user_id = users.id" +
                " WHERE users.login = ?")) {
            pst.setString(1,user.getLogin());
            rs = pst.executeQuery();
            while (rs.next()){
                teams.add(new Team(rs.getString("name")));
            }
        }catch (SQLException ex){
            logger.log(Level.SEVERE,ex.getMessage());
        }finally {
            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.log(Level.SEVERE,e.getMessage());
                }
            }
        }
        return teams;
    }

    public void deleteTeam(Team team){
        int id = team.getId();
        PreparedStatement pst = null;
        try{
            pst = con.prepareStatement("DELETE FROM teams WHERE id = ?");
            pst.setInt(1,id);
            pst.execute();
        }catch (SQLException | NullPointerException ex){
            logger.log(Level.SEVERE,ex.getMessage());
        }finally {
            if(pst != null){
                try {
                    pst.close();
                } catch (SQLException e) {
                    logger.log(Level.SEVERE,e.getMessage());
                }
            }
        }
    }

    public void updateTeam(Team team){
        PreparedStatement pst =null;
        try{
            pst = con.prepareStatement("UPDATE teams SET name = ? WHERE id = ?");
            pst.setString(1,team.getName());
            pst.setInt(2,team.getId());
            pst.execute();
        }catch (SQLException | NullPointerException ex){
            logger.log(Level.SEVERE,ex.getMessage());
        }finally {
            if(pst != null){
                try {
                    pst.close();
                } catch (SQLException e) {
                    logger.log(Level.SEVERE,e.getMessage());
                }
            }
        }
    }

}
