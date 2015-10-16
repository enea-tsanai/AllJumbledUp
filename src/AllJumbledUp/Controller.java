package AllJumbledUp;

import com.sun.jnlp.ApiDialog;
import com.sun.jnlp.ApiDialog.DialogResult;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

import java.util.Optional;
import java.util.Set;

//TODO: Add comments
public class Controller {

    @FXML
    private GridPane JumbledWordsA1;

    @FXML
    private GridPane JumbledWordsA2;

    @FXML
    private Label storyLabel;

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
    public void bindData () {
        int l = 0;

        /* Populate Area A1 */
        for (JumbledWord jw : allJumbledUp.getJumbledWords()) {

            /* Jumbled Words labels */
            Label jwLabel = new Label(jw.getJumbledWord());
            jwLabel.setFont(Font.font("Monospaced", 18));
            jwLabel.setMaxWidth(100);

            /* Input field for user to guess the jumbled word*/
            TextField guessField = new TextField();
            guessField.setId("gjw_" + l);
            guessField.setFont(Font.font("Monospaced", 17));
            guessField.setStyle("-fx-background-color: transparent;");

            /* Background non editable field to highlight special letters*/
            TextField guessFieldSL = new TextField();
            //guessFieldSL.setId("");
            guessFieldSL.setFont(Font.font("Monospaced", 18));
            //guessFieldSL.setText("\u0A66");
            guessFieldSL.setText(jw.getMask());
            guessFieldSL.setEditable(false);
            guessFieldSL.setStyle("-fx-text-inner-color: red;");
            addTextLimiter(guessField, jw.getJumbledWord().length());
            addTextMatchController(guessField, jw.getWord(), jw.getSPchars());

            JumbledWordsA1.add(jwLabel, 0, l);
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
        fguessField.setFont(Font.font("Monospaced", 17));
        fguessField.setStyle("-fx-background-color: transparent;");

        /* Background non editable field to highlight special letters*/
        TextField fguessFieldSL = new TextField();
        //guessFieldSL.setId("");
        fguessFieldSL.setFont(Font.font("Monospaced", 18));
        //guessFieldSL.setText("\u0A66");
        fguessFieldSL.setText("_");
        fguessFieldSL.setEditable(false);
        fguessFieldSL.setStyle("-fx-text-inner-color: red;");

        JumbledWordsA2.add(fjwLabel, 0, 0);
        JumbledWordsA2.add(fguessFieldSL, 0, 1);
        JumbledWordsA2.add(fguessField, 0, 1);

        storyLabel.setText(AllJumbledUp.getStory());
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
                    tf.setStyle("-fx-text-inner-color: green;");

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
