package AllJumbledUp;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class Controller {
    @FXML
    private TableView<JumbledWord> JumbledWordsA1;
    @FXML
    private TableColumn<JumbledWord, String> JumbledColumA1;
    @FXML
    private TableColumn<JumbledWord, String> GuessColumnA1;

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
        // Initialize the person table with the two columns.
        JumbledColumA1.setCellValueFactory(cellData -> cellData.getValue().getJumbledWordProperty());
        GuessColumnA1.setCellValueFactory(cellData -> cellData.getValue().getWordProperty());
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param allJumbledUpApp
     */
    public void setMainApp(AllJumbledUp allJumbledUpApp) {
        this.allJumbledUp = allJumbledUpApp;

        // Add observable list data to the table
        JumbledWordsA1.setItems(allJumbledUp.getJumbledWords());
    }
}
