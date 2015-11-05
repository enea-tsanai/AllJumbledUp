package AllJumbledUp;

import javafx.fxml.FXML;

/**
 * Created by enea on 11/5/15.
 */
public class FacebookLoginPageController {
    // Reference to the main application.
    private AllJumbledUp allJumbledUp;

    public FacebookLoginPageController() {
    }

    public void setMainApp(AllJumbledUp allJumbledUpApp) {
        System.out.println("Controller started");
        this.allJumbledUp = allJumbledUpApp;
    }
}
