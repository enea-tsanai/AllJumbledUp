package AllJumbledUp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

/**
 * Created by enea on 10/9/15.
 */
public class MenuController {

    private AllJumbledUp allJumbledUp;

    @FXML
    private TextField numOfPlayers;

    @FXML
    private TextField difficultyLevel;

    @FXML
    private CheckBox sound;

    @FXML
    private void initialize() {}

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.print("Button clicked!");
        allJumbledUp.setNumOfPlayers(Integer.parseInt(numOfPlayers.getText()));
        allJumbledUp.setDifficultyLevelLevel(Integer.parseInt((difficultyLevel.getText())));
        allJumbledUp.setSound(sound.isSelected());

//        System.out.println(Integer.parseInt(difficultyLevel.getText()));
//        System.out.println(sound.isSelected());

        allJumbledUp.showMainGameScene();
    }

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public MenuController() {
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param allJumbledUpApp
     */
    public void setMainApp(AllJumbledUp allJumbledUpApp) {
        System.out.println("Controller started");
        this.allJumbledUp = allJumbledUpApp;
        //bindData();
    }

}
