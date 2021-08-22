package com.epam.rd.java.basic.practice8.db.entity;

public class Team {

    private String name;
    private int id;

    public Team(String name){
        this.name = name;
    }

    public Team(int id, String name){
        this.id = id;
        this.name =name;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public static Team createTeam(String name){
        return new Team(name);
    }

    @Override
    public String toString() {
       return name;
    }



}
