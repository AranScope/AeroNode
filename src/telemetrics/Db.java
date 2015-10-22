package telemetrics;

import system.BCrypt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Db Class
 * @author Aran Long
 *
 * Db provides access methods to allow the creation, editing and deletion of new Users
 * from the User database. Through the BCrypt library user data is encrypted to ensure data security.
 */

public class Db {
    private String dbUser = "";
    private String dbPass = "";
    private String dbAddress = "";

    /**
     * Sets up connection to the database.
     * Retrieves password salt from database.
     * Hashes password to check against stored username and stored encrypted password password.
     * Returns true if entered username and password combination match stored username and password, when encrypted.
     *
     * @param username
     * @param password
     * @return
     */
    public boolean loginUser(String username, String password){
        try {
            Statement statement;
            ResultSet resultSet;

            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(dbAddress,dbUser,dbPass);

            //attempting to retrieve password Salt from User table in database
            String query = "SELECT Salt FROM User WHERE Username='" + username + "'";
            statement = con.createStatement();
            resultSet = statement.executeQuery(query);
            String salt;
            if(resultSet != null) {
                resultSet.next();
                salt = resultSet.getString("Salt");
            }
            else return false; //User or Salt does not exist in database, User can not be logged in.
            System.out.println(salt);

            String hashedPassword = BCrypt.hashpw(password, salt); //hashing user password with BCrypt library and retrieved password Salt.
            query = "SELECT * FROM User WHERE Username='" + username + "' AND HashedPassword ='" + hashedPassword + "'";
            statement = con.createStatement();
            resultSet = statement.executeQuery(query);

            if (resultSet.next()) return true; //Hashed password matches password stored in User table in database.
        }catch(Exception e){e.printStackTrace();}

        return false; //Database not found or User does not exist.
    }

    /**
     * Sets up connection to the database.
     * Checks whether entered username exists in a record in the User table in the database.
     * Returns true if username exists in the User table.
     *
     * @param username
     * @return
     */
    public boolean userExists(String username){
        try {
            Statement statement;
            ResultSet resultSet;

            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(dbAddress,dbUser,dbPass);

            //attempting to retrieve User from User table in database
            String query = "SELECT * FROM User WHERE Username='" + username + "'";
            statement = con.createStatement();
            resultSet = statement.executeQuery(query);
            if(resultSet != null) {
                return true;
            }
        }catch(Exception e){e.printStackTrace();}
        return false;
    }

    /**
     * Sets up connection to the database.
     * Calls userExists method to ensure username is not already taken.
     * Generates a random encryption salt for the user.
     * Hashes password with generated encryption salt.
     * Inserts new User record into User table with username, hashedPassword, email, salt fields.
     *
     * @param username
     * @param password
     * @param email
     * @return
     */
    public boolean signUpUser(String username, String password, String email){
        try {
            Statement statement;

            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(dbAddress,dbUser,dbPass);
            String randomSalt = BCrypt.gensalt(); //generating a new random password salt for the User.
            System.out.println(randomSalt);
            String hashedPassword = BCrypt.hashpw(password, randomSalt); //Hashing the users password with the generated Salt.

            if (userExists(username)){ //ensuring that user does not already exist in the User table in the database.
                return false;
            }
            else{
                String query = "INSERT INTO User (Username, Email, Salt, HashedPassword) VALUES('" + username + "','" + email + "','" + randomSalt + "','" + hashedPassword + "');"; //Inserting new record in User table with Username, Email, Salt and HashedPassword
                statement = con.createStatement();
                statement.executeUpdate(query);
                return true;
            }
        }catch(Exception e){e.printStackTrace();}
        return false;
    }
}