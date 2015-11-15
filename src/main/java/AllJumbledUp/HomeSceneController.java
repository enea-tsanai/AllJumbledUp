package AllJumbledUp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Created by enea.
 * Date: 11/10/15.
 * Time: 2:40 AM.
 */

public class HomeSceneController {

    @FXML
    Button FBLogin;

    @FXML
    Button FreePlay;

    private AllJumbledUp allJumbledUp;

    @FXML
    private void handleFBLogin(ActionEvent event) {
        allJumbledUp.setGameMode(AllJumbledUp.GameMode.FacebookUser);
        allJumbledUp.showFBLoginScene();
    }

    @FXML
    private void handleFreePlay(ActionEvent event) {
        allJumbledUp.setGameMode(AllJumbledUp.GameMode.FreePlay);
        allJumbledUp.showMainMenuScene();
    }

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public HomeSceneController() {
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param allJumbledUpApp
     */
    public void setMainApp(AllJumbledUp allJumbledUpApp) {
        System.out.println("GameSceneController started");
        this.allJumbledUp = allJumbledUpApp;
    }

}
