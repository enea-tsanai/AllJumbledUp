package AllJumbledUp;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

//TODO: Add comments
public class AllJumbledUp extends Application {

    public enum DifficultyLevel {
        EASY, MEDIUM, HIGH
    }

    /* Game Settings */
    private static int numOfPlayers = 1;
    private static DifficultyLevel difficultyLevel = DifficultyLevel.EASY ;
    private static boolean sound = false;

    /* Words Dictionaries */
    private List <JumbledWord> JumbledWords = new ArrayList<>();
    private List <FwStoryPair> FWDictionary = new ArrayList<>();
    private static String JW, Story;

    /* Game Stage */
    public Stage stage;

    /*Constructor */
    public AllJumbledUp() {
        genFWDictionary();
        //for (int i = 0; i < 50; i++)
        assignFW();
        genJumbledWords();
    }

    /* Assign Final Word */
    public void assignFW() {
        int index = ThreadLocalRandom.current().nextInt(1, FWDictionary.size());
        JW = FWDictionary.get(index).getFW();
        Story = FWDictionary.get(index).getStory();
        System.out.println("FW: " + JW);
    }

    /* Generates Final words dictionary */
    private void genFWDictionary() {
        FWDictionary.add(new FwStoryPair("Potato", "this is a story"));
        FWDictionary.add(new FwStoryPair("Application", "this is a story"));
        FWDictionary.add(new FwStoryPair("Monkey", "this is a story"));
        FWDictionary.add(new FwStoryPair("Aeroplane", "this is a story"));
        FWDictionary.add(new FwStoryPair("Football", "this is a story"));
        FWDictionary.add(new FwStoryPair("Building", "this is a story"));
    }

    /* Generate the game's jumbled words */
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

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public boolean getSound() {
        return sound;
    }

    public void setDifficultyLevelLevel(DifficultyLevel lvl) {
        difficultyLevel = lvl;
    }

    public void setNumOfPlayers(int players) {
        numOfPlayers = players;
    }

    public void setSound(boolean isSoundOn) {
        sound = isSoundOn;
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        showMainMenuScene(primaryStage);
        //showMainGameScene();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void showMainMenuScene(Stage stage) {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("MainMenu.fxml"));

            Parent root = loader.load();

            MenuController controller = loader.getController();
            controller.setMainApp(this);

//            String css = this.getClass().getResource("AllJumbledUp.css").toExternalForm();
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

            String css = this.getClass().getResource("AllJumbledUp.css").toExternalForm();
            Scene JumbleScene = new Scene(root);
            JumbleScene.getStylesheets().add(css);

            stage.setTitle("All Jumbled Up");
            stage.setScene(JumbleScene);
            stage.setResizable(false);
            stage.show();

            JumbleScene.getWindow().setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent ev) {
                    if (!controller.shutdown()) {
                        ev.consume();
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
