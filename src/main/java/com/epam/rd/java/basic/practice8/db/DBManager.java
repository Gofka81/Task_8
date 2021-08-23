package com.epam.rd.java.basic.practice8.db;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
        PreparedStatement pst = null;
        if(user != null)
            try {
                pst = con.prepareStatement(Constant.INSERT_USER);
                pst.setString(1, user.getLogin());
                pst.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public List<User> findAllUsers(){
        List<User> users = new ArrayList<>();
        Statement st = null;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery(Constant.SELECT_ALL_USERS);

            while (rs.next()){
                users.add(new User(rs.getString("login")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void insertTeam(Team team) {
        PreparedStatement pst = null;
        if(team != null) {
            try {
                pst = con.prepareStatement(Constant.INSERT_TEAM);
                pst.setString(1, team.getName());
                pst.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Team> findAllTeams() {
        List<Team> teams = new ArrayList<>();
        Statement st = null;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery(Constant.SELECT_ALL_TEAMS);

            while (rs.next()){
                teams.add(new Team(rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teams;
    }

    public Team getTeam(String name) {
        PreparedStatement pst = null;
        try {
            pst = con.prepareStatement(Constant.GET_TEAM);
            pst.setString(1,name);
            ResultSet rs = pst.executeQuery();
            if(rs.next())
                return new Team(rs.getInt("id"),rs.getString("name"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(pst != null)
                    pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public User getUser(String login)  {
        PreparedStatement pst = null;
        try{
            pst = con.prepareStatement(Constant.GET_USER);
            pst.setString(1,login);
            ResultSet rs = pst.executeQuery();
            if(rs.next())
                return new User(rs.getInt("id"),rs.getString("login"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(pst != null)
                    pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Connection getConnection(String connectionUrl){
        try {
            con = DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public void setTeamsForUser(User user, Team... teams){
        if(user != null) {
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
                        ex.printStackTrace();
                    } finally {
                        try {
                            con.setAutoCommit(true);
                        } catch (SQLException ex) {
                                ex.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public List<Team> getUserTeams(User user){
        List<Team> teams = new ArrayList<>();
        try(PreparedStatement pst = con.prepareStatement(Constant.GET_TEAMS_FROM_USER)) {
            pst.setString(1,user.getLogin());
            ResultSet rst = pst.executeQuery();
            while (rst.next()){
                teams.add(new Team(rst.getString("name")));
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return teams;
    }

    public void deleteTeam(Team team){
        int id = team.getId();
        try(PreparedStatement pst = con.prepareStatement(Constant.DELETE_TEAM)){
            pst.setInt(1,id);
            pst.execute();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public void updateTeam(Team team){
        int id = team.getId();
        try(PreparedStatement pst = con.prepareStatement(Constant.UPDATE_TEAM)){
            pst.setString(1,team.getName());
            pst.setInt(2,team.getId());
            pst.execute();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

}
