package AllJumbledUp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * Created by enea on 10/9/15.
 */
public class MenuController {

    private AllJumbledUp allJumbledUp;

    @FXML
    private ComboBox numOfPlayers;

    @FXML
    private ComboBox difficultyLevel;

    @FXML
    private CheckBox sound;

    @FXML
    private void initialize() {}

    @FXML
    private void bindData() {
        /* Set number of players combobox); */
        ObservableList<Integer> numOfPlayersOpts = FXCollections.observableArrayList(1, 2);
        numOfPlayers.setItems(numOfPlayersOpts);
        numOfPlayers.getSelectionModel().selectFirst();

        /* Set difficulty level options */
        ObservableList<AllJumbledUp.DifficultyLevel> difficultyOpts =
                FXCollections.observableArrayList(AllJumbledUp.DifficultyLevel.values());
        difficultyLevel.setItems(difficultyOpts);
        difficultyLevel.getSelectionModel().selectFirst();

        sound.setSelected(allJumbledUp.getSound());
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.print("Button clicked!");
        allJumbledUp.setNumOfPlayers(Integer.parseInt(numOfPlayers.getSelectionModel().getSelectedItem().toString()));
        allJumbledUp.setDifficultyLevelLevel((AllJumbledUp.DifficultyLevel) difficultyLevel.getSelectionModel().getSelectedItem());
        allJumbledUp.setSound(sound.isSelected());

//        System.out.println(allJumbledUp.getNumOfPlayers());
//        System.out.println(allJumbledUp.getDifficultyLevel().toString());
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
        bindData();
    }

}
