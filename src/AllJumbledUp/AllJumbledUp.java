package AllJumbledUp;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AllJumbledUp extends Application {

    private ObservableList<JumbledWord> JumbledWords = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource("AllJumbledUp.fxml"));

        // Give the controller access to the main app.
        Controller controller = loader.getController();
        controller.setMainApp(this);

        primaryStage.setTitle("All Jumbled Up");
        primaryStage.setScene(new Scene(root, 600, 465));
        primaryStage.show();
    }

    public void genJumbledWords() {
        JumbledWords.add(new JumbledWord("TestA"));
        JumbledWords.add(new JumbledWord("TestB"));
    }

    public ObservableList<JumbledWord> getJumbledWords() {
        return JumbledWords;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
