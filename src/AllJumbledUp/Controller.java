package AllJumbledUp;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

import java.util.Optional;

//TODO: Add comments
public class Controller {

    @FXML
    private GridPane JumbledWordsA1;

    @FXML
    private GridPane JumbledWordsA2;

    @FXML
    private ScrollPane StoryPane;

    /* Final Jumbled Word label */
    @FXML
    static Label fjwLabel = new Label();

    static TextField fguessField = new TextField();

    // Reference to the main application.
    private AllJumbledUp allJumbledUp;
    private static int FoundJwords = 0;
    private static boolean FWrevealed = false;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public Controller() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        System.out.println("Controller Initialized");
        JumbledWordsA1.getStyleClass().add("grid");
        JumbledWordsA2.getStyleClass().add("grid");
    }

    @FXML
    private void bindData () {
        int l = 0;

        /* Populate Area A1 */
        for (JumbledWord jw : allJumbledUp.getJumbledWords()) {

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

            /* Add to Grid Area */
            JumbledWordsA1.add(jwLabel, 0, l);
            JumbledWordsA1.setHalignment(jwLabel, HPos.RIGHT);
            JumbledWordsA1.add(guessFieldSL, 1, l);
            JumbledWordsA1.add(guessField, 1, l);

            l++;
        }

        /* Populate Area A2 */
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
        fguessField.setEditable(false);
        fguessField.setId("gjw_" + l);
        fguessField.setFont(Font.font("Monospaced", 18));
        fguessField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent");
        fguessField.setPadding(new Insets(5, 5, 5, 5));

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

    }

    // Listeners
    /*Limits max input field length and acceptable characters*/
    public void addTextLimiter(final TextField tf, final String cf, final int maxLength) {
        tf.textProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue.length() > 0) {
                char charPressed = newValue.charAt(newValue.length() - 1);
                int occInNewVal = countOccurrencesOf(tf.getText(), charPressed);
                int occInJumbledW = countOccurrencesOf(cf, charPressed);

                if ((tf.getText().length() > maxLength) || (occInNewVal > occInJumbledW)) {
                    tf.setText(oldValue);
                }
            }
        });
    }

    public int countOccurrencesOf(String a, char b) {
        int counter = 0;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) == b) {
                counter++;
            }
        }
        return counter;
    }

    /*Checks if our input is identical to the jumbled world*/
    public void addTextMatchController(final TextField tf, final String keyW, final String spchars, final boolean isFinalWord) {
        tf.textProperty().addListener((ov, oldValue, newValue) -> {
            if (tf.getText().equalsIgnoreCase(keyW)) {

                tf.setEditable(false);
                tf.setStyle("-fx-text-fill: green; -fx-background-color: transparent");

                if (isFinalWord) {
                    gameOverDialog();
                }
                else {
                    // Add the special characters of this word above the final jumbled word
                    if (FoundJwords == 0)
                        fjwLabel.setText("");
                    fjwLabel.setText(fjwLabel.getText() + spchars);

                    FoundJwords++;

                    if ((FoundJwords == 4) && (!FWrevealed)){
                        fguessField.setEditable(true);
                    }
                }
            }
        });
    }

    /* Confirm Game Exit */
    public boolean shutdown () {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("Exit the Game");
        alert.setContentText("Are you ok with this?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            return true;
        } else {
            return false;
        }
    }

    /* Game Over Dialog */
    public void gameOverDialog () {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Game Over!");
        alert.setHeaderText("You found the hidden word! Congratulations!");

        ButtonType exit = new ButtonType("Exit Game");
        ButtonType rePlay = new ButtonType("New Game");

        alert.getButtonTypes().setAll(exit, rePlay);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == rePlay) {
            allJumbledUp.showMainMenuScene();
        } else {
            allJumbledUp.gameOver();
        }
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
