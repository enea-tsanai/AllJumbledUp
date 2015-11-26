package AllJumbledUp;

import javafx.animation.KeyFrame;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.animation.Timeline;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.Optional;
import javafx.fxml.FXML;

//TODO: Add comments
public class GameSceneController {

    @FXML
    private GridPane JumbledWordsA1;

    @FXML
    private GridPane JumbledWordsA2;

    @FXML
    private ScrollPane StoryPane;

    /* Final Jumbled Word label */
    @FXML
    static Label fjwLabel;

    @FXML
    private Label Timer;

    @FXML
    private Label Score;

    static TextField fguessField;
    private Timeline gameTimer;

    // Reference to the main application.
    private AllJumbledUp allJumbledUp;
    private static int FoundJwords = 0;
    private static boolean FWrevealed = false;
    private static int lettersFound = 0;
    private static int timeRemaining = 0;


    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public GameSceneController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        FoundJwords = 0;
        FWrevealed = false;
        System.out.println("GameSceneController Initialized");
        JumbledWordsA1.getStyleClass().add("grid");
//        JumbledWordsA1.setGridLinesVisible(true);
        JumbledWordsA2.getStyleClass().add("grid");
    }

    @FXML
    private void bindData () {

        int l = 0;

        /* Populate Area A1 */
        for (JumbledWord jw : allJumbledUp.getJumbledWords()) {

            //todo: check input field width calculation
            Text JW = new Text(jw.getJumbledWord());
            JW.setFont(Font.font("Monospaced", 18));
            JW.setStyle("-fx-border-color: blue;");
            double inputWidth = JW.getLayoutBounds().getWidth() + 20;

            /* Jumbled Words labels */
            Label jwLabel = new Label(jw.getJumbledWord());
            jwLabel.setFont(Font.font("Monospaced", 18));
            jwLabel.setStyle("-fx-border-color: blue;");
            jwLabel.setPadding(new Insets(5, 5, 5, 5));

            /* Input field for user to guess the jumbled word*/
            TextField guessField = new TextField();
            guessField.setId("gjw_" + l);
            guessField.setFont(Font.font("Monospaced", 18));
            guessField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent");
            guessField.setPadding(new Insets(5, 5, 5, 5));

            /* Background non editable field to highlight special letters*/
            Label guessFieldSL = new Label();
            guessFieldSL.setText(jw.getMask());
//            guessFieldSL.setStyle("-fx-text-color: red;");
            guessFieldSL.setFont(Font.font("Monospaced", 18));
            guessFieldSL.setStyle("-fx-border-color: blue;");
            guessFieldSL.setPadding(new Insets(5, 5, 5, 5));
            guessFieldSL.setMaxWidth(jwLabel.getMaxWidth());
            guessFieldSL.setPrefWidth(jwLabel.getPrefWidth());

            addTextLimiter(guessField, jw.getWord(), jw.getJumbledWord().length());
            addTextMatchController(guessField, jw.getWord(), jw.getSPchars(), false);

            guessField.setMaxWidth(inputWidth);

            /* Add to Grid Area */
            JumbledWordsA1.add(jwLabel, 0, l);
            JumbledWordsA1.setHalignment(jwLabel, HPos.RIGHT);
            JumbledWordsA1.add(guessFieldSL, 1, l);
            JumbledWordsA1.add(guessField, 1, l);

            l++;
        }

        /* Populate Area A2 */
        fjwLabel = new Label();
        fjwLabel.setText(AllJumbledUp.getJW().replaceAll(".", "*"));
        fjwLabel.setFont(Font.font("Monospaced", 18));
        fjwLabel.setPadding(new Insets(5, 5, 0, 5));
//        fjwLabel.setMaxWidth(200);

        /* Background non editable field to highlight special letters*/
        Label fguessFieldSL = new Label(AllJumbledUp.getJW().replaceAll(".", "_"));
        fguessFieldSL.setFont(Font.font("Monospaced", 18));
        fguessFieldSL.setStyle("-fx-border-color: blue;");
        fguessFieldSL.setPadding(new Insets(5, 5, 5, 5));

        /* Input field for user to guess the jumbled word*/
        fguessField = new TextField();
        fguessField.clear();
        fguessField.setEditable(false);
        fguessField.setId("gjw_" + l);
        fguessField.setFont(Font.font("Monospaced", 18));
        fguessField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent");
        fguessField.setPadding(new Insets(5, 5, 5, 5));

        //todo: check input field width calculation
//        JumbledWordsA2.setGridLinesVisible(true);
        Text FWInput = new Text(AllJumbledUp.getJW());
        FWInput.setFont(Font.font("Monospaced", 18));
        FWInput.setStyle("-fx-border-color: blue;");
        double FWinputWidth = FWInput.getLayoutBounds().getWidth() + 20;
        fguessField.setMaxWidth(FWinputWidth);

        System.out.println("TextDelimeter Test| fguessField---> " + fguessField.getText() + " AllJumbledUp.getJW() "
                + AllJumbledUp.getJW());
        addTextLimiter(fguessField, AllJumbledUp.getJW(), AllJumbledUp.getJW().length());
        addTextMatchController(fguessField, AllJumbledUp.getJW(), null, true);

        JumbledWordsA2.add(fjwLabel, 0, 0);
        JumbledWordsA2.add(fguessFieldSL, 0, 1);
        JumbledWordsA2.add(fguessField, 0, 1);

        /* Story Area */
        Label storyLabel = new Label(AllJumbledUp.getStory());
        storyLabel.setWrapText(true);
        storyLabel.setStyle("-fx-font-family: \"Comic Sans MS\"; -fx-font-size: 14; -fx-text-fill: darkred;");

        /* Add to Scroll Pane */
        StoryPane.setContent(storyLabel);
        StoryPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        StoryPane.setFitToWidth(true);

        Score.setText("Score: 0");
        Timer.setText(Integer.toString(allJumbledUp.getTimer()));
        updateTimer();

    }

    public void updateTimer() {
        gameTimer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            Timer.setText(allJumbledUp.updateTimer());
        }));
        gameTimer.setCycleCount(allJumbledUp.getTimer());
        gameTimer.setOnFinished(event -> Platform.runLater(() -> gameOverDialog(AllJumbledUp.ExitFlag.TIME_UP)));
        gameTimer.play();
    }

    public void updateScore(String score) {
        Score.setText("Score: " + score);
    }

    // Text Filters
    /*Limits max input field length and acceptable characters*/
    public void addTextLimiter(final TextField tf, final String cf, final int maxLength) {
        tf.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().isEmpty()) {

                char charPressed = event.getCharacter().charAt(0);
                int occInNewVal = countOccurrencesOf(tf.getText(), charPressed);
                int occInJumbledW = countOccurrencesOf(cf, charPressed);

                if ((tf.getText().length() + 1 > maxLength) || (occInNewVal + 1 > occInJumbledW)) {
                    event.consume();
                    AllJumbledUp.sounds.get("invalidChar").play();
                } else
                    AllJumbledUp.sounds.get("keyPressed").play();
            }
        });
    }

    public int countOccurrencesOf(String a, char b) {
        int counter = 0;
        for (int i = 0; i < a.length(); i++) {
            if (Character.toLowerCase(a.charAt(i)) == Character.toLowerCase(b)) {
                counter++;
            }
        }
        return counter;
    }

    /*Checks if our input is identical to the jumbled world*/
    public void addTextMatchController(final TextField tf, final String keyW, final String spchars,
                                       final boolean isFinalWord) {
        tf.textProperty().addListener((ov, oldValue, newValue) -> {
            if (tf.getText().equalsIgnoreCase(keyW)) {

                tf.setEditable(false);
                tf.setStyle("-fx-text-fill: green; -fx-background-color: transparent");

                if (isFinalWord) {
                    gameTimer.stop();
                    allJumbledUp.updateScore(0 , allJumbledUp.getTimer(), true);
                    Score.setText("Score: " + Integer.toString(allJumbledUp.getScore()));

                    if (AllJumbledUp.getGameMode() == AllJumbledUp.GameMode.FacebookUser) {
                        allJumbledUp.saveScore();
                    }

                    gameOverDialog(AllJumbledUp.ExitFlag.SOLVED_RIDDLE);
                }
                else {
                    // Add the special characters of this word above the final jumbled word
                    if (FoundJwords == 0)
                        fjwLabel.setText("");
                    String fTmp = fjwLabel.getText() + spchars;
                    fjwLabel.setText(JumbledWord.jumble(AllJumbledUp.getJW().substring(0, fTmp.length()), fTmp));

                    FoundJwords++;
                    lettersFound += keyW.length();
                    allJumbledUp.updateScore(lettersFound, 0 , false);
                    updateScore(Integer.toString(allJumbledUp.getScore()));

                    AllJumbledUp.sounds.get("foundWord").play();

                    if ((FoundJwords == 4) && (!FWrevealed)){
                        fguessField.setEditable(true);
                        AllJumbledUp.sounds.get("unlockedFW").play();
                    }
                }
            } else if (tf.getText().length() == keyW.length() && !tf.getText().equalsIgnoreCase(keyW)) {
                tf.setStyle("-fx-text-fill: red; -fx-background-color: transparent");
            } else {
                tf.setStyle("-fx-text-fill: black; -fx-background-color: transparent");
            }
        });
    }

    /* Confirm Game Exit */
    public boolean shutdown () {
        gameTimer.pause();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("Exit the Game");
        alert.setContentText("Are you ok with this?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            return true;
        } else {
            gameTimer.play();
            return false;
        }
    }

    /* Game Over Dialog */
    public void gameOverDialog (AllJumbledUp.ExitFlag flag) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Game Over!");

        switch (flag) {
            case SOLVED_RIDDLE:
                alert.setHeaderText("You solved the riddle! Congratulations!\n" + "Your Score is " +
                        allJumbledUp.getScore() + ".");
                break;
            case TIME_UP:
                alert.setHeaderText("Time is up!");
                break;
            default:
        }

        ButtonType exit = new ButtonType("Exit Game");
        ButtonType rePlay = new ButtonType("New Game");
        ButtonType postOnFB = new ButtonType("Post Score on Facebook");

        switch (AllJumbledUp.getGameMode()) {
            case FreePlay:
                alert.getButtonTypes().setAll(exit, rePlay);
                break;
            case FacebookUser:
                if (flag == AllJumbledUp.ExitFlag.SOLVED_RIDDLE) {
                    alert.setWidth(550);
                    alert.getButtonTypes().setAll(exit, rePlay, postOnFB);
                }
                else
                    alert.getButtonTypes().setAll(exit, rePlay);
                break;
        }

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == rePlay)
            allJumbledUp.showMainMenuScene();
        else if (result.get() == postOnFB) {
            allJumbledUp.postScoreOnFacebook();
            alert.getButtonTypes().setAll(exit, rePlay);
            alert.setHeaderText("Score has been posted! What next?");
            Optional<ButtonType> result2 = alert.showAndWait();
            if (result2.get() == rePlay)
                allJumbledUp.showMainMenuScene();
            else
                allJumbledUp.gameOver();
        }
        else
            allJumbledUp.gameOver();
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
