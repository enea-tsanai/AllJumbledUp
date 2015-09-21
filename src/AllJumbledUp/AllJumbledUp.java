package AllJumbledUp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.List;

//TODO: Add comments
public class AllJumbledUp extends Application {

    private List <JumbledWord> JumbledWords = new LinkedList<>();

    public AllJumbledUp() {
        genJumbledWords();
    }

    public void genJumbledWords() {
        //TODO: Implement Logic
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
}
