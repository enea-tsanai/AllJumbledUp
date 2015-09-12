package AllJumbledUp;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class Controller {
    @FXML
    private TableView<JumbledWord> JumbledWordsA1;
    @FXML
    private TableColumn<JumbledWord, String> JumbledColumnA1;
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
        JumbledWordsA1.setEditable(true);
        JumbledColumnA1.setCellValueFactory(cellData -> cellData.getValue().getJumbledWordProperty());
        GuessColumnA1.setCellValueFactory(new PropertyValueFactory<>("GuessWord"));
        GuessColumnA1.setCellFactory(TextFieldTableCell.<JumbledWord>forTableColumn());
//        GuessColumnA1.setCellValueFactory(cellData -> cellData.getValue().getGuessWordProperty());

//
////        // Custom rendering of the table cell.
//        GuessColumnA1.setCellFactory(column -> {
//            return new TableCell<JumbledWord, String>() {
//                @Override
//                protected void updateItem(String item, boolean empty) {
////                    super.updateItem(item, empty);
//                    this.setEditable(true);
//                    if (item == null || empty) {
//                        setText("sdf");
//                        setStyle("");
//                    } else if (item.equals("top")) {
//                        // Format date.
//                        setText("TEST");
//                        setTextFill(Color.CHOCOLATE);
//                        setStyle("-fx-background-color: yellow");
//                    }
////                    setText("TEST");
////                    setTextFill(Color.CHOCOLATE);
////                    setStyle("-fx-background-color: yellow");
//                }
//            };
//        });
//
//
//
//
//        private TableColumn createLastNameColumn() {
//            Callback<TableColumn, TableCell> editableFactory = new Callback<TableColumn, TableCell>() {
//                @Override
//                public TableCell call(TableColumn p) {
//                    return new EditingCell();
//                }
//            };
//
//            TableColumn lastNameCol = new TableColumn("Last Name");
//            lastNameCol.setMinWidth(100);
//            lastNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("lastName"));
//            lastNameCol.setCellFactory(editableFactory);
//            lastNameCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Person, String>>() {
//                @Override
//                public void handle(TableColumn.CellEditEvent<Person, String> t) {
//                    System.out.println("Commiting last name change. Previous: " + t.getOldValue() + "   New: " + t.getNewValue());
//                    t.getRowValue().setLastName(t.getNewValue());
//                }
//            });
//
//            return lastNameCol;
//        }





        // Listeners
        GuessColumnA1.setOnEditCommit((TableColumn.CellEditEvent<JumbledWord, String> t) -> {
            System.out.println("Edited");

            if (((JumbledWord) t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                ).getWord().equals(t.getNewValue())) {
                System.out.println("Found It!!");
            }
        });

//        emailCol.setOnEditCommit(
//                (CellEditEvent<Person, String> t) -> {
//                    ((Person) t.getTableView().getItems().get(
//                            t.getTablePosition().getRow())
//                    ).setEmail(t.getNewValue());
//                });


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
