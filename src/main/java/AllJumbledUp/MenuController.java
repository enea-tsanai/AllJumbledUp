package AllJumbledUp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


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
    private ImageView UserPicture;

    @FXML
    private void initialize() {}

    @FXML
    private void bindData() {
        UserPicture.setImage(new Image(Session.getUserPicture()));
//        UserPicture.setImage(new Image("https://scontent.xx.fbcdn.net/hprofile-xpf1/v/t1.0-1/c0.6.50.50/p50x50/12065971_1162641033750447_2679819914534391594_n.jpg?oh=fa19d170324c4da089d96f594a0036a8&oe=56F8AC75"));

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
        allJumbledUp.startGame();
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
