package system;

import uicomponents.Login;
/**
 * system.Main Class
 * @author Aran Long
 *
 * This class creates instances of the login and controller classes, the login JFrame is displayed whilst the user is not logged
 * in and is disposed of in place of the main JFrame located within the controller class once the user has successfully logged in.
 */

public class Main {

    /**
     * Program entry, creates login interface and main program controller instances.
     * @param args
     */
    public static void main(String[] args) {
        //createLoginInterface();
        createProgramController();
    }


    /**
     * Creates Login interface instance, checks for login status, returns when user has logged in.
     */
    private static void createLoginInterface(){
        Login login = new Login();
        boolean loggedIn = false; //boolean stating whether user has logged in.

        while(!loggedIn) { //while the user has not logged in.
            loggedIn = login.isLoggedIn(); //assign current login state to loggedIn boolean.
        }

        login.frame.dispose(); //remove login interface.
    }

    //procedure to create the main program controller object.

    /**
     * creates Controller instance.
     */
    private static void createProgramController(){
        Controller controller = new Controller();
    }
}