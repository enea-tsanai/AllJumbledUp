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
    static Label fjwLabel = new Label("**********");

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

            addTextLimiter(guessField, jw.getJumbledWord().length());
            addTextMatchController(guessField, jw.getWord(), jw.getSPchars());

            /* Add to Grid Area */
            JumbledWordsA1.add(jwLabel, 0, l);
            JumbledWordsA1.setHalignment(jwLabel, HPos.RIGHT);
            JumbledWordsA1.add(guessFieldSL, 1, l);
            JumbledWordsA1.add(guessField, 1, l);

            l++;
        }

        /* Populate Area A2 */

        fjwLabel.setFont(Font.font("Monospaced", 18));
        fjwLabel.setMaxWidth(200);

        /* Input field for user to guess the jumbled word*/
        TextField fguessField = new TextField();
        fguessField.setId("gjw_" + l);
        fguessField.setFont(Font.font("Monospaced", 18));
        fguessField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent");

        JumbledWordsA2.add(fjwLabel, 0, 0);
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
    /*Limits max input field length*/
    public static void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (tf.getText().length() > maxLength) {
                    String s = tf.getText().substring(0, maxLength);
                    tf.setText(s);
                }
            }
        });
    }

    /*Checks if our input is identical to the jumbled world*/
    public static void addTextMatchController(final TextField tf, final String keyW, final String spchars) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (tf.getText().equals(keyW)) {

                    tf.setEditable(false);
                    tf.setStyle("-fx-text-fill: green; -fx-background-color: transparent");

                    // Add the special characters of this word above the final jumbled word
                    if(FoundJwords == 0)
                        fjwLabel.setText("");
                    fjwLabel.setText(fjwLabel.getText()+spchars);

                    FoundJwords ++;

//                    if ((FoundJwords == 4) && (!FWrevealed)){
//                        fjwLabel.setText(AllJumbledUp.getJW());
//                    }
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
