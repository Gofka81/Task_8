package com.epam.rd.java.basic.practice8.db.entity;

import java.util.Objects;

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

    public void setId(int id){
        this.id = id;
    }

    public static User createUser(String login){
        return new User(login);
    }

    @Override
    public String toString() {
        return login;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){return true;}
        if(obj != null ||obj.getClass() != getClass()){return false;}
        User user = (User) obj;
        return getLogin().equals(user.getLogin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }
}
