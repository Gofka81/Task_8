package com.epam.rd.java.basic.practice8.db.entity;

public class User {
    private String login;
    private int id;

    public User(String login){
        this.login=login;
    }

    public User(int id, String login){
        this.id = id;
        this.login = login;
    }

    public String getLogin(){
        return login;
    }

    public int getId() {
        return id;
    }

    public static User createUser(String login){
        return new User(login);
    }

    @Override
    public String toString() {
        return login;
    }
}
