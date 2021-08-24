package com.epam.rd.java.basic.practice8.db.entity;

import java.util.Objects;

public class Team {

    private String name;
    private int id;

    public Team(){}

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

    public void setId(int id){
        this.id = id;
    }

    public static Team createTeam(String name){
        return new Team(name);
    }

    @Override
    public String toString() {
       return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){return true;}
        if(obj == null || getClass() != obj.getClass()){return false;}
        Team team = (Team) obj;
        return Objects.equals(name, team.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
