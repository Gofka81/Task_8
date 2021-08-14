

Practical task 8

_______________________

Note.

1. The User class should contain:

 1) the method 'getLogin()', which returns the login of the user;

 2) the method 'toString()', which returns the login of the user;

 3) the implementation of the 'equals(Object)' method, according to which two User objects are equal if and only if they have the same login.

2. The Team class should contain:

 1) the method 'getName()', which returns the name of the group;

 2) the method 'toString()', which returns the name of the group;

 3) the implementation of the 'equals(Object)' method, according to which two Team objects are equal if and only if they have the same name. 

3. Receive the connection by calling the 'DriverManager.getConnection(CONNECTION_URL)' method. CONNECTION_URL - is a connection string that includes login and password of the user. Read the connection string from the file 'app.properties' using the key 'connection.url'. The file should be located at the root of the project.

Example of the ‘app.properties’ file content:

—————————————————————————

connection.url = jdbc:mysql://localhost:3306/p8db?user=user&password=userpass

—————————————————————————

Note. Do not load the driver class manually (referred to a call to 'Class.forName (JDBC-DRIVER-FQN)', exclude such lines from the code).

4.  The method 'setTeamsForUser (User, Team ...)' should be implemented using a transaction: as a result of calling this method, the user will be assigned either all groups or none. 

If the method is called like this: setTeamsForUser (user, teamA, teamB, teamC), then the entries in the users_teams relationship table should be inserted sequentially in the order that the groups appear in the argument list from left to right:

user_id, teamA_id 

user_id, teamB_id  

user_id, teamC_id

If the last record cannot be added, then the first two also do not get into the database (rollback transaction).  

Script to create a test database (for Derby):

—————————————————————————

CREATE TABLE users (  

 id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,  

 login VARCHAR(10) UNIQUE 

); 

CREATE TABLE teams (  

 id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,  

 name VARCHAR(10) 

); 

CREATE TABLE users_teams (  

 user_id INT REFERENCES users(id) ON DELETE CASCADE,  

 team_id INT REFERENCES teams(id) ON DELETE CASCADE,  

 UNIQUE (user_id, team_id) 

);

—————————————————————————

5. Note, the DBManager#getUserTeams method should return a List <Team> object.

_______________________

Database.  

Use any relational database as a database.  

The database contains three tables: 

users (id, login) 

teams (id, name) 

users_teams (user_id, team_id)  

Initially, the database tables should have some content (see the class code of the Demo class)  

Create a sql directory at the root and save the database creation script in it (db-create.sql)

_______________________

Demo class (copy it to your project)

—————————————————————————

package com.epam.rd.java.basic.practice8;  

import java.util.List;  

import com.epam.rd.java.basic.practice8.db.DBManager; 

import com.epam.rd.java.basic.practice8.db.entity.Team; 

import com.epam.rd.java.basic.practice8.db.entity.User;  

public class Demo {

      private static void printList(List<?> list) {

         System.out.println(list);

     }

      public static void main(String[] args) {

         // users  ==> [ivanov] 

        // teams ==> [teamA]

          DBManager dbManager = DBManager.getInstance();

          // Part 1

           dbManager.insertUser(User.createUser(“petrov”));

 dbManager.insertUser(User.createUser(“obama"));

 printList(dbManager.findAllUsers());

         // users  ==> [ivanov, petrov, obama] 

         System.out.println(“===========================");

          // Part 2

           dbManager.insertTeam(Team.createTeam(“teamB”));

 dbManager.insertTeam(Team.createTeam("teamC"));      

 printList(dbManager.findAllTeams());

         // teams ==> [teamA, teamB, teamC]          

System.out.println(“===========================");

          // Part 3

         User userPetrov = dbManager.getUser(“petrov");

         User userIvanov = dbManager.getUser(“ivanov");

         User userObama = dbManager.getUser(“obama");

          Team teamA = dbManager.getTeam(“teamA");

         Team teamB = dbManager.getTeam(“teamB");

         Team teamC = dbManager.getTeam(“teamC");

          // method setTeamsForUser must implement transaction!        

dbManager.setTeamsForUser(userIvanov, teamA);         dbManager.setTeamsForUser(userPetrov, teamA, teamB);         dbManager.setTeamsForUser(userObama, teamA, teamB, teamC);

          for (User user : dbManager.findAllUsers()) {

           printList(dbManager.getUserTeams(user));          

 System.out.println(“~~~~~");

         }

         // teamA

         // teamA teamB

         // teamA teamB teamC       

   System.out.println(“===========================");

   // Part 4

  // on delete cascade!

    dbManager.deleteTeam(teamA);

    // Part 5

         teamC.setName(“teamX");

         dbManager.updateTeam(teamC);

         printList(dbManager.findAllTeams());

         // teams ==> [teamB, teamX]

      }

 }

—————————————————————————

In all tasks, create and implement the corresponding types in such a way so that when you started the Demo class, the corresponding functionality would work.

Task 1

 _______________________

The DBManager#insertUser method should modify the ‘id’ field of the User object. 

The DBManager#findAllUsers method returns a java.util.List <User> object. _______________________

Task 2 

_______________________

The DBManager#insertTeam method should modify the ‘id’ field of the Team object. 

The DBManager#findAllTeams method returns a java.util.List <Team> object. 

_______________________  

Task 3 

_______________________  

The DBManager#setTeamsForUser method should implement the transaction. 

Properly implement the commit / rollback transaction logic. 

The DBManager#getUserTeams method returns a java.util.List <Team> object.

_______________________

Task 4 

_______________________  

The DBManager#deleteTeam method deletes a group by name. 

All child entries from the users_teams table must also be deleted. 

Implement the latter using cascading referential integrity constraints: 

ON DELETE CASCADE. 

_______________________   

Task 5 

_______________________  

The DBManager#updateTeam method updates the group.

_______________________

IMPORTANT! If you cover your project with tests, you have to follow these requirements:

1. Use h2 database instead of MySQL. eMentor DOES NOT USE MySQL but h2.

2. You have to create app.properties before test being launched. The example of app.properties initialization is below.

public class Part1Test {

    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:~/test";
    private static final String URL_CONNECTION = "jdbc:h2:~/test;user=youruser;password=yourpassword;";
    private static final String USER = "youruser";
    private static final String PASS = "yourpassword";

    @Spy //actually this annotation is not necessary here
    private static DBManager dbManager;

    @BeforeClass
    public static void beforeTest() throws SQLException, ClassNotFoundException {
        Class.forName(JDBC_DRIVER);

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
            String sql = "CREATE TABLE IF NOT EXISTS users (\n" +
                    "  id INTEGER(11) NOT NULL AUTO_INCREMENT,\n" +
                    " login VARCHAR(20) NOT NULL, \n" +
                    "  PRIMARY KEY (id));";

            statement.executeUpdate(sql);
        }
    }
}

