package com.epam.rd.java.basic.practice8;

import com.epam.rd.java.basic.practice8.db.DBManager;
import com.epam.rd.java.basic.practice8.db.constant.Constant;
import com.epam.rd.java.basic.practice8.db.entity.Team;
import com.epam.rd.java.basic.practice8.db.entity.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class Demo {

    private static void printList(List<?> list) {

        System.out.println(list);

    }

    public static void main(String[] args) {

        // users  ==> [ivanov]

        // teams ==> [teamA]

        DBManager dbManager = DBManager.getInstance();


        FileInputStream fis = null;
        Properties properties = new Properties();
        try {
            fis = new FileInputStream(Constant.CONNECTION_FILE_NAME);

            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        Connection connection = dbManager.getConnection(properties.getProperty(Constant.URL_KEY));
            System.out.println("Successful");

            // Part 1

            dbManager.insertUser(User.createUser("ivanov"));
            dbManager.insertUser(User.createUser("petrov"));
            dbManager.insertUser(User.createUser("obama"));

            printList(dbManager.findAllUsers());


            // users  ==> [ivanov, petrov, obama]

            System.out.println("===========================");

            // Part 2
            dbManager.insertTeam(Team.createTeam("teamA"));
            dbManager.insertTeam(Team.createTeam("teamB"));
            dbManager.insertTeam(Team.createTeam("teamC"));

            printList(dbManager.findAllTeams());

            // teams ==> [teamA, teamB, teamC]
            System.out.println("===========================");

            // Part 3

            User userPetrov = dbManager.getUser("petrov");

            User userIvanov = dbManager.getUser("ivanov");

            User userObama = dbManager.getUser("obama");

            Team teamA = dbManager.getTeam("teamA");

            Team teamB = dbManager.getTeam("teamB");

            Team teamC = dbManager.getTeam("teamC");


                                                        // method setTeamsForUser must implement transaction!
        dbManager.setTeamsForUser(userIvanov, teamA);
        dbManager.setTeamsForUser(userPetrov, teamA, teamB);
        dbManager.setTeamsForUser(userObama, teamA, teamB, teamC);

        for (User user : dbManager.findAllUsers()) {

            printList(dbManager.getUserTeams(user));
            System.out.println("~~~~~");

        }

        // teamA

        // teamA teamB

        // teamA teamB teamC
        System.out.println("===========================");

// Part 4

// on delete cascade!

        dbManager.deleteTeam(teamA);

// Part 5

        teamC.setName("teamX");

        dbManager.updateTeam(teamC);

        printList(dbManager.findAllTeams());

        // teams ==> [teamB, teamX]

    }
}
