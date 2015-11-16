package AllJumbledUp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.bson.Document;

import java.util.ArrayList;

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
    private HBox settings;

    @FXML
    private void initialize() {}

    @FXML
    private void bindData() {
        /* Facebook Authorized User */
        if (AllJumbledUp.getGameMode() == AllJumbledUp.GameMode.FacebookUser) {
            UserPicture.setImage(new Image(Session.getUserPicture()));
            //UserPicture.setImage(new Image("https://scontent.xx.fbcdn.net/hprofile-xpf1/v/t1.0-1/c0.6.50.50/p50x50/12065971_1162641033750447_2679819914534391594_n.jpg?oh=fa19d170324c4da089d96f594a0036a8&oe=56F8AC75"));

            /* My Previous Scores */
            GridPane myPreviousScores = new GridPane();
            myPreviousScores.setGridLinesVisible(true);
            myPreviousScores.setAlignment(Pos.CENTER);
            myPreviousScores.setPrefWidth(600);
            myPreviousScores.add(new Label("DATE AND TIME"), 1, 0);
            myPreviousScores.add(new Label("MY SCORES"), 0, 0);

            ArrayList <Document> scores = DbManager.getMyScoreHistory();

            for (int i=0; i<scores.size() && i < 5; i++) {
                Label dateTime = new Label(scores.get(i).get("Score").toString());
                Label score = new Label(scores.get(i).get("DateTime").toString());
                myPreviousScores.add(dateTime, 0, i+1);
                myPreviousScores.add(score, 1, i+1);
            }
            settings.getChildren().add(myPreviousScores);
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
