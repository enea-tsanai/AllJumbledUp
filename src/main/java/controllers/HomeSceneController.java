package controllers;

import AllJumbledUp.GameManager;
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

    private GameManager gameManager;

    @FXML
    private void handleFBLogin(ActionEvent event) {
        gameManager.setGameMode(GameManager.GameMode.FacebookUser);
        gameManager.showFBLoginScene();
    }

    @FXML
    private void handleFreePlay(ActionEvent event) {
        gameManager.setGameMode(GameManager.GameMode.FreePlay);
        gameManager.showMainMenuScene();
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
     * @param gameManagerApp
     */
    public void setMainApp(GameManager gameManagerApp) {
        System.out.println("GameSceneController started");
        this.gameManager = gameManagerApp;
    }

}
