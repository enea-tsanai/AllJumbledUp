package AllJumbledUp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
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
    private ToolBar topToolBar;

    @FXML
    private HBox settings;

    @FXML
    private TabPane MainTabPanel;

    @FXML
    private void initialize() {}

    @FXML
    private void bindData() {
        /* Facebook Authorized User */
        if (AllJumbledUp.getGameMode() == AllJumbledUp.GameMode.FacebookUser) {

            ImageView UserPic = new ImageView();
            UserPic.setImage(new Image(Session.getUserPicture()));
            UserPic.setFitHeight(60);
            UserPic.setPreserveRatio(true);

            topToolBar.getItems().add(UserPic);

            Label userName = new Label(Session.getFullName());
            //UserPicture.setImage(new Image("https://scontent.xx.fbcdn.net/hprofile-xpf1/v/t1.0-1/c0.6.50.50/p50x50/12065971_1162641033750447_2679819914534391594_n.jpg?oh=fa19d170324c4da089d96f594a0036a8&oe=56F8AC75"));

            /* My Previous Scores */
            GridPane myPreviousScores = new GridPane();
            myPreviousScores.setGridLinesVisible(true);
            myPreviousScores.setAlignment(Pos.CENTER);
            myPreviousScores.setMaxWidth(Double.MAX_VALUE);//setPrefWidth(400);
            GridPane.setHgrow(myPreviousScores, Priority.ALWAYS);
            myPreviousScores.add(new Label("DATE AND TIME"), 1, 0);
            myPreviousScores.add(new Label("MY SCORES"), 0, 0);

            ArrayList <Document> scores = DbManager.getMyScoreHistory();

            for (int i=0; i<scores.size() && i < 5; i++) {
                Label dateTime = new Label(scores.get(i).get("Score").toString());
                Label score = new Label(scores.get(i).get("DateTime").toString());
                myPreviousScores.add(dateTime, 0, i+1);
                myPreviousScores.add(score, 1, i+1);
            }

            /* High Scores Tables */
            GridPane HighScoresTable = new GridPane();
            HighScoresTable.setGridLinesVisible(true);
            HighScoresTable.setAlignment(Pos.CENTER);
            HighScoresTable.setPrefWidth(400);
            HighScoresTable.add(new Label("DATE AND TIME"), 2, 0);
            HighScoresTable.add(new Label("MY SCORES"), 1, 0);
            HighScoresTable.add(new Label("PLAYER"), 0, 0);

            ArrayList <Document> highScores = DbManager.getPlayersHighscores();

            for (int i=0; i<highScores.size() && i < 5; i++) {
                Label name = new Label(highScores.get(i).get("FullName").toString());
                Label score = new Label(highScores.get(i).get("Score").toString());
                Label dateTime = new Label(highScores.get(i).get("DateTime").toString());

                HighScoresTable.add(name, 0, i+1);
                HighScoresTable.add(score, 1, i+1);
                HighScoresTable.add(dateTime, 2, i+1);
            }


//            settings.getChildren().add(HighScoresTable);

            HBox playerHbox = new HBox();
            VBox scoresVbox = new VBox();
            VBox playerVBox = new VBox();

            ImageView playerPic = new ImageView();
            playerPic.setImage(new Image(Session.getUserPicture()));
            playerPic.setFitHeight(170);
            playerPic.setPreserveRatio(true);

            playerVBox.getChildren().add(playerPic);
            playerVBox.getChildren().add(userName);
            playerVBox.setAlignment(Pos.CENTER);
            playerVBox.setSpacing(20);


            scoresVbox.getChildren().add(new Label("My Previous Scores"));
            scoresVbox.getChildren().add(myPreviousScores);
            scoresVbox.getChildren().add(new Label("Best Players"));
            scoresVbox.getChildren().add(HighScoresTable);
            scoresVbox.setAlignment(Pos.CENTER);
            scoresVbox.setSpacing(30);


            playerHbox.getChildren().add(playerVBox);
            playerHbox.getChildren().add(scoresVbox);
            playerHbox.setAlignment(Pos.CENTER);
            playerHbox.setFillHeight(true);
            playerHbox.setSpacing(30);
            playerHbox.setMaxWidth(Double.MAX_VALUE);

            Tab playerTab = new Tab("PLAYER");
            playerTab.setStyle("HBox.hgrow=\"ALWAYS\"");
            playerTab.setContent(playerHbox);
            MainTabPanel.getTabs().add(playerTab);
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
