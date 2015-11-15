package AllJumbledUp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;

/**
 * Created by enea.
 * Date: 10/9/15.
 * Time: 2:18 AM.
 */

public class MenuController {

    private AllJumbledUp allJumbledUp;

    @FXML
    private ComboBox<AllJumbledUp.DifficultyLevel> difficultyLevel;

    @FXML
    private CheckBox sound;

    @FXML
    private ImageView UserPicture;

    @FXML
    private void initialize() {}

    @FXML
    private void bindData() {

        if (allJumbledUp.getGameMode() == AllJumbledUp.GameMode.FacebookUser) {
            UserPicture.setImage(new Image(Session.getUserPicture()));
            //UserPicture.setImage(new Image("https://scontent.xx.fbcdn.net/hprofile-xpf1/v/t1.0-1/c0.6.50.50/p50x50/12065971_1162641033750447_2679819914534391594_n.jpg?oh=fa19d170324c4da089d96f594a0036a8&oe=56F8AC75"));
        }

        /* Set difficulty level options */
        ObservableList<AllJumbledUp.DifficultyLevel> difficultyOpts =
                FXCollections.observableArrayList(AllJumbledUp.DifficultyLevel.values());
        difficultyLevel.setItems(difficultyOpts);
        difficultyLevel.getSelectionModel().selectFirst();

        sound.setSelected(allJumbledUp.getSound());
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        allJumbledUp.setDifficultyLevelLevel(difficultyLevel.getSelectionModel().getSelectedItem());
        allJumbledUp.setSound(sound.isSelected());
        allJumbledUp.startGame();
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
        System.out.println("GameSceneController started");
        this.allJumbledUp = allJumbledUpApp;
        bindData();
    }

}
