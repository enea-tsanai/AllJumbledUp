package AllJumbledUp;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class Controller {
    @FXML
    private GridPane JumbledWordsA1;

    @FXML
    private Label storyLabel;

    // Reference to the main application.
    private AllJumbledUp allJumbledUp;

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
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(20);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(80);
    }

    @FXML
    public void bindData () {
        int l = 0;
        for (JumbledWord jw : allJumbledUp.getJumbledWords()) {
            System.out.println(jw.getJumbledWord());

            Label jwLabel = new Label(jw.getJumbledWord());
            jwLabel.setFont(Font.font("Monospaced", 25));
            jwLabel.setMaxWidth(100);

                /* Input field for user to guess the jumbled word*/
            TextField guessField = new TextField();
            guessField.setId("gjw_" + l);
            guessField.setFont(Font.font("Monospaced", 24));
            guessField.setStyle("-fx-background-color: transparent;");

            /* Background non editable field to highlight special letters*/
            TextField guessFieldSL = new TextField();
            //guessFieldSL.setId("");
            guessFieldSL.setFont(Font.font("Monospaced", 25));
            //guessFieldSL.setText("\u0A66");
            guessFieldSL.setText("_");
            guessFieldSL.setEditable(false);
            guessFieldSL.setStyle("-fx-text-inner-color: red;");
            addTextLimiter(guessField, jw.getJumbledWord().length());
            addTextMatchController(guessField, jw.getWord());

            JumbledWordsA1.add(jwLabel, 0, l);
            JumbledWordsA1.add(guessFieldSL, 1, l);
            JumbledWordsA1.add(guessField, 1, l);

            l++;
        }
    }

    // Listeners
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

    public static void addTextMatchController(final TextField tf, final String keyW) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (tf.getText().equals(keyW)) {
                    tf.setEditable(false);
                    tf.setStyle("-fx-text-inner-color: green;");
                    System.out.println(tf.getAlignment().getHpos());
                }
            }
        });
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
