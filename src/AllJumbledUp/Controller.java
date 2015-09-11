package AllJumbledUp;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

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
        System.out.println("Controller Initialized");
        // Initialize the tables
        JumbledColumA1.setCellValueFactory(cellData -> cellData.getValue().getJumbledWordProperty());
        GuessColumnA1.setCellFactory(TextFieldTableCell.forTableColumn());

        // Listeners
        GuessColumnA1.setOnEditCommit((TableColumn.CellEditEvent<JumbledWord, String> t) -> {
            System.out.println("Edited");
            if(t.getNewValue().equals("papa")) {
                System.out.println("Found It!!");
            }
        });

//        GuessColumnA1.setOnEditCancel((TableColumn.CellEditEvent<JumbledWord, String> t) -> {
//            t.getTableColumn().getCellValueFactory().getProperty();
//            (t.getTableView().getItems().get(t.getTablePosition().getRow())).(t.getNewValue());
//        });
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
