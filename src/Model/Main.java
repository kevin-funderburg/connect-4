package Model;
import View.mainMenu;

public class Main {


    //TODO:
    // - implement sound effects and the toggle for it
    // - make everything pretty?
    // - make a win tracker to see score in one session
    // - look through SRS and look if we are missing any other requirement/method

    public static void main(String[] args) {

        mainMenu window = new mainMenu();
        window.menu.setVisible(true);

    }
}