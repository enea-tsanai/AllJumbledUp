package AllJumbledUp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.List;

public class AllJumbledUp extends Application {

//    private ObservableList<JumbledWord> JumbledWords = FXCollections.observableArrayList();
    private List <JumbledWord> JumbledWords = new LinkedList<>();


    public AllJumbledUp() {
        genJumbledWords();
    }

    public void genJumbledWords() {
        JumbledWords.add(new JumbledWord("TestA"));
        JumbledWords.add(new JumbledWord("TestB"));
        JumbledWords.add(new JumbledWord("TestC"));
        JumbledWords.add(new JumbledWord("TestD"));
    }

    public List<JumbledWord> getJumbledWords() {
        return JumbledWords;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        getJumbledWords();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AllJumbledUp.fxml"));
        Parent root = loader.load();

        // Give the controller access to the main app.
        Controller controller = loader.getController();
        controller.setMainApp(this);

        primaryStage.setTitle("All Jumbled Up");
        primaryStage.setScene(new Scene(root, 600, 465));
        primaryStage.show();
    }

    /**
     * Initializes the root layout.
     */
//    public void initRootLayout() {
//        try {
//            // Load root layout from fxml file.
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(AllJumbledUp.class.getResource("AllJumbledUp.fxml"));
//            Parent rootLayout = loader.load(getClass().getResource("AllJumbledUp.fxml"));
//
//            // Show the scene containing the root layout.
//            Scene scene = new Scene(rootLayout);
//            primaryStage.setScene(scene);
//            primaryStage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
