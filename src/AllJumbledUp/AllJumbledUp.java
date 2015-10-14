package AllJumbledUp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

//TODO: Add comments
public class AllJumbledUp extends Application {

    private List <JumbledWord> JumbledWords = new ArrayList<>();
    private List <FwStoryPair> FWDictionary = new ArrayList<>();
    private static String JW, Story;

    public Stage stage;

    /* Assign Final Word */
    public void assignFW() {
        int index = ThreadLocalRandom.current().nextInt(1, FWDictionary.size());
        JW = FWDictionary.get(index).getFW();
        Story = FWDictionary.get(index).getStory();
        System.out.println("FW: " + JW);
    }

    /*Constructor */
    public AllJumbledUp() {
        genFWDictionary();
        //for (int i = 0; i < 50; i++)
            assignFW();
        genJumbledWords();
    }


    private void genFWDictionary() {
        FWDictionary.add(new FwStoryPair("Potato", "this is a story"));
        FWDictionary.add(new FwStoryPair("Application", "this is a story"));
        FWDictionary.add(new FwStoryPair("Monkey", "this is a story"));
        FWDictionary.add(new FwStoryPair("Aeroplane", "this is a story"));
        FWDictionary.add(new FwStoryPair("Football", "this is a story"));
        FWDictionary.add(new FwStoryPair("Building", "this is a story"));
    }

    public void genJumbledWords() {
        //TODO: Implement Logic
        List<Integer> sps = Arrays.asList(1,2);
        JumbledWords.add(new JumbledWord("abcd", sps));
        sps = Arrays.asList(0,2);
        JumbledWords.add(new JumbledWord("efgh", sps));
        sps = Arrays.asList(3);
        JumbledWords.add(new JumbledWord("ijkl", sps));
        sps = Arrays.asList(0);
        JumbledWords.add(new JumbledWord("mnopq", sps));
    }

    public List<JumbledWord> getJumbledWords() {
        return JumbledWords;
    }

    public static String getJW() {
        return JW;
    }

    public static String getStory() {
        return Story;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        showMainMenuScene(primaryStage);
        //showMainGameScene();
    }

    public void showMainMenuScene(Stage stage) {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("MainMenu.fxml"));

            Parent root = loader.load();

            MenuController controller = loader.getController();
            controller.setMainApp(this);

//            String css = this.getClass().getResource("style.css").toExternalForm();
            Scene JumbleScene = new Scene(root);
//            JumbleScene.getStylesheets().add(css);

            stage.setTitle("All Jumbled Up - Main Menu");
            stage.setScene(JumbleScene);
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showMainGameScene() {
        try {

            FXMLLoader loader = new FXMLLoader();//(getClass().getResource("AllJumbledUp.fxml"));
            loader.setLocation(getClass().getResource("AllJumbledUp.fxml"));

            Parent root = loader.load();
            Controller controller = loader.getController();
            controller.setMainApp(this);

            String css = this.getClass().getResource("style.css").toExternalForm();
            Scene JumbleScene = new Scene(root);
            JumbleScene.getStylesheets().add(css);

            stage.setTitle("All Jumbled Up");
            stage.setScene(JumbleScene);
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
