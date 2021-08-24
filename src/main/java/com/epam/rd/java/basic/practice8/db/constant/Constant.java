package com.epam.rd.java.basic.practice8.db.constant;

public class Constant {
    private Constant(){

    }
    public static final String URL_KEY = "connection.url";
    public static final String CONNECTION_FILE_NAME = "app.properties";

    public static final String INSERT_USER = "INSERT INTO  users(login) VALUES(?)";
    public static final String INSERT_TEAM = "INSERT INTO  teams(name) VALUES(?)";

    public static final String SELECT_ALL_USERS = "SELECT login FROM users ORDER BY id";
    public static final String SELECT_ALL_TEAMS = "SELECT name FROM teams ORDER BY id";

    public static final String GET_USER = "SELECT id,login FROM users WHERE login = ?";
    public static final String GET_TEAM = "SELECT id,name FROM teams WHERE name = ?";

    public static final String SET_TEAMS_FOR_USER = "INSERT INTO users_teams(teams_id,users_id) VALUES(?,?)";
    public static final String GET_TEAMS_FROM_USER = "SELECT teams.id, teams.name FROM users_teams" +
            " INNER JOIN teams ON users_teams.team_id = teams.id" +
            " INNER JOIN users ON users_teams.user_id = users.id" +
            " WHERE users.login = ?";

    public static final String DELETE_TEAM = "DELETE FROM teams WHERE id = ?";
    public static final String UPDATE_TEAM = "UPDATE teams SET name = ? WHERE id = ?";
}
