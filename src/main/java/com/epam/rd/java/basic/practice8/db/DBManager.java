package com.epam.rd.java.basic.practice8.db;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.epam.rd.java.basic.practice8.db.constant.Constant;
import com.epam.rd.java.basic.practice8.db.entity.Team;
import com.epam.rd.java.basic.practice8.db.entity.User;

public class DBManager {

    private static DBManager dbManager;
    private static Connection con;

    private DBManager() {
    }

    public static DBManager getInstance() {
        if(dbManager == null){
            dbManager = new DBManager();
        }
        return dbManager;
    }

    public void insertUser(User user)  {
        Logger logger = Logger.getAnonymousLogger();
        try(PreparedStatement pst = con.prepareStatement(Constant.INSERT_USER, Statement.RETURN_GENERATED_KEYS)){
            pst.setString(1, user.getLogin());
            pst.execute();
            ResultSet rs = pst.getGeneratedKeys();
            if(rs.next()){
                int id = rs.getInt(1);
                user.setId(id);
            }
        } catch (SQLException | NullPointerException e) {
            logger.log(Level.SEVERE,e.getMessage());
        }
    }

    public List<User> findAllUsers(){
        Logger logger = Logger.getAnonymousLogger();
        List<User> users = new ArrayList<>();
        try (Statement st = con.createStatement()){
            ResultSet rs = st.executeQuery(Constant.SELECT_ALL_USERS);

            while (rs.next()){
                users.add(new User(rs.getString("login")));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE,e.getMessage());
        }
        return users;
    }

    public void insertTeam(Team team) {
        Logger logger = Logger.getAnonymousLogger();
        try(PreparedStatement pst =con.prepareStatement(Constant.INSERT_TEAM,Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, team.getName());
            pst.execute();
            ResultSet rst = pst.getGeneratedKeys();
            if(rst.next()){
                int id = rst.getInt(1);
                team.setId(id);
            }
        } catch (SQLException | NullPointerException e) {
            logger.log(Level.SEVERE,e.getMessage());
        }
    }

    public List<Team> findAllTeams() {
        Logger logger = Logger.getAnonymousLogger();
        List<Team> teams = new ArrayList<>();
        Statement st = null;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery(Constant.SELECT_ALL_TEAMS);

            while (rs.next()){
                teams.add(new Team(rs.getString("name")));
            }
        } catch (SQLException | NullPointerException e) {
            logger.log(Level.SEVERE,e.getMessage());
        }
        return teams;
    }

    public Team getTeam(String name) {
        Logger logger = Logger.getAnonymousLogger();
        PreparedStatement pst = null;
        try {
            pst = con.prepareStatement(Constant.GET_TEAM);
            pst.setString(1,name);
            ResultSet rs = pst.executeQuery();
            if(rs.next())
                return new Team(rs.getInt("id"),rs.getString("name"));
        } catch (SQLException | NullPointerException e) {
            logger.log(Level.SEVERE,e.getMessage());
        }
        finally {
            try {
                if(pst != null)
                    pst.close();
            } catch (SQLException e) {
                logger.log(Level.SEVERE,e.getMessage());
            }
        }
        return null;
    }

    public User getUser(String login)  {
        Logger logger = Logger.getAnonymousLogger();
        PreparedStatement pst = null;
        try{
            pst = con.prepareStatement(Constant.GET_USER);
            pst.setString(1,login);
            ResultSet rs = pst.executeQuery();
            if(rs.next())
                return new User(rs.getInt("id"),rs.getString("login"));
        } catch (SQLException | NullPointerException e) {
            logger.log(Level.SEVERE,e.getMessage());
        }
        finally {
            try {
                if(pst != null)
                    pst.close();
            } catch (SQLException e) {
                logger.log(Level.SEVERE,e.getMessage());
            }
        }
        return null;
    }

    public Connection getConnection(String connectionUrl){
        Logger logger = Logger.getAnonymousLogger();
        try {
            con = DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            logger.log(Level.SEVERE,e.getMessage());
        }
        return con;
    }

    public void setTeamsForUser(User user, Team... teams){
        if(user != null) {
            Logger logger = Logger.getAnonymousLogger();
            int id = user.getId();
            for (Team team : teams) {
                try (PreparedStatement preparedStatement = con.prepareStatement(Constant.SET_TEAMS_FOR_USER)) {
                    con.setAutoCommit(false);
                    preparedStatement.setInt(1, team.getId());
                    preparedStatement.setInt(2, id);
                    con.commit();
                    preparedStatement.execute();
                } catch (SQLException | NullPointerException e) {
                    try {
                        con.rollback();
                    } catch (SQLException ex) {
                        logger.log(Level.SEVERE,ex.getMessage());;
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
    }

    public List<Team> getUserTeams(User user){
        Logger logger = Logger.getAnonymousLogger();
        List<Team> teams = new ArrayList<>();
        try(PreparedStatement pst = con.prepareStatement(Constant.GET_TEAMS_FROM_USER)) {
            pst.setString(1,user.getLogin());
            ResultSet rst = pst.executeQuery();
            while (rst.next()){
                teams.add(new Team(rst.getString("name")));
            }
        }catch (SQLException ex){
            logger.log(Level.SEVERE,ex.getMessage());
        }
        return teams;
    }

    public void deleteTeam(Team team){
        Logger logger = Logger.getAnonymousLogger();
        int id = team.getId();
        try(PreparedStatement pst = con.prepareStatement(Constant.DELETE_TEAM)){
            pst.setInt(1,id);
            pst.execute();
        }catch (SQLException | NullPointerException ex){
            logger.log(Level.SEVERE,ex.getMessage());
        }
    }

    public void updateTeam(Team team){
        Logger logger = Logger.getAnonymousLogger();
        int id = team.getId();
        try(PreparedStatement pst = con.prepareStatement(Constant.UPDATE_TEAM)){
            pst.setString(1,team.getName());
            pst.setInt(2,team.getId());
            pst.execute();
        }catch (SQLException | NullPointerException ex){
            logger.log(Level.SEVERE,ex.getMessage());
        }
    }

}
