package AllJumbledUp;

import facebook4j.Facebook;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.*;


//TODO: Add comments
public class AllJumbledUp extends Application {

    public enum DifficultyLevel {
        EASY, MEDIUM, HIGH
    }

    public static enum ExitFlag {
        TIME_UP, SOLVED_RIDDLE, EXIT_GAME
    }

    public static enum GameMode {
        FacebookUser, FreePlay
    }

    public static Facebook facebook;
    public static Session session;
    private static GameMode gameMode;
    private static int Score = 0;

    /* Game Settings */
    private static int numOfPlayers = 1;
    private static DifficultyLevel difficultyLevel = DifficultyLevel.EASY;
    private static boolean sound = false;
    private static int timer; //Timer in seconds

    /* Words Dictionaries */
    private List <JumbledWord> JumbledWords = new ArrayList<>();
    private List <FwStoryPair> FWDictionary = new ArrayList<>();
    private static String JW, Story;
    private DbManager db;

    /* Game Stage */
    public Stage stage;

    /*Constructor */
    public AllJumbledUp() {
        db = new DbManager("AllJumbledUp");
//        db.cleanDB();
        session = new Session();
    }

    public void startGame() {
        db.generateFinalWordStoryPair();
        assignFW(db.getFinalWordStoryPair());
        assignJumbledWords(db.getJumbledWords());
        setTimer();
    }

    //TODO: Check everything that needs to go to dev/null!
    public void restart() {
        assignFW(db.getFinalWordStoryPair());
        assignJumbledWords(db.getJumbledWords());
    }

    /* Assign Final Word */
    public void assignFW(ArrayList<String> key_pair) {
        JW = key_pair.get(0);
        Story = key_pair.get(1);
        System.out.println("FW: " + JW + " Riddle: " + Story);
    }

    //Todo:Remove
    /* Generates Final words dictionary */
    private void genFWDictionary() {
        FWDictionary.add(new FwStoryPair("Potato", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."));
    }

    /* Generate the game's jumbled words */
    public void assignJumbledWords(ArrayList<ArrayList<String>> jumbleWordPairs) {
        if(!JumbledWords.isEmpty()) {
            JumbledWords.clear();
        }
        for (ArrayList<String> jumbleWordPair : jumbleWordPairs) {
            JumbledWords.add(new JumbledWord(jumbleWordPair.get(0), jumbleWordPair.get(1)));
        }
    }

    public List<JumbledWord> getJumbledWords() {
        long seed = System.nanoTime();
        /* Shuffle list */
        Collections.shuffle(JumbledWords, new Random(seed));
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

    private void setTimer() {
        switch (difficultyLevel) {
            case EASY:
                timer = 240;
                break;
            case MEDIUM:
                timer = 240;
                break;
            case HIGH:
                timer = 240;
                break;
            default:
                timer = 240;
        }
    }

    public GameMode getGameMode () {
        return gameMode;
    }

    public void setGameMode (GameMode gm) {
        gameMode = gm;
    }

    public String updateTimer() {
        timer --;
        String m = Integer.toString(timer / 60);
        m = (m.length()<2)? "0"+m: m ;
        String s = Integer.toString(timer % 60);
        s = (s.length()<2)? "0"+s: s ;
        return m+":"+s;
    }

    public int getTimer(){
        return timer;
    }

    /* Temp Score shown while playing */
    public int getTempScore (int lettersFound) {
        int pointsPerLetter = 5;
        Score = lettersFound * pointsPerLetter;
        return Score;
    }

    /* Final score when the riddle is found */
    public int getScore(int timeRemaining) {
        int baseScore;
        int pointsPerTimeRemaining = 5;

        switch (difficultyLevel) {
            case EASY:
                baseScore = 100;
                break;
            case MEDIUM:
                baseScore = 300;
                break;
            case HIGH:
                baseScore = 500;
                break;
            default:
                baseScore = 100;
        }
        Score += baseScore + timeRemaining * pointsPerTimeRemaining;
        return Score;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        showHomeScene();
//        showFBLoginScene();
//        Facebook facebook = new FacebookFactory().getInstance();
//        System.out.println(facebook);
//        System.out.println(facebook.getOAuthAppAccessToken());

//        showMainMenuScene();
//        showMainGameScene();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void showMainMenuScene() {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/MainMenu.fxml"));

            Parent root = loader.load();

            MenuController controller = loader.getController();
            controller.setMainApp(this);

            String css = this.getClass().getResource("/MainMenu.css").toExternalForm();
            Scene JumbleScene = new Scene(root);
            JumbleScene.getStylesheets().add(css);

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
            loader.setLocation(getClass().getResource("/AllJumbledUp.fxml"));

            Parent root = loader.load();
            Controller controller = loader.getController();
            controller.setMainApp(this);

            String css = this.getClass().getResource("/AllJumbledUp.css").toExternalForm();
            Scene JumbleScene = new Scene(root);
            JumbleScene.getStylesheets().add(css);

            stage.setTitle("All Jumbled Up");
            stage.setScene(JumbleScene);
            stage.setResizable(false);
            stage.show();

            JumbleScene.getWindow().setOnCloseRequest(ev -> {
                if (!controller.shutdown()) {
                    ev.consume();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showFBLoginScene() {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/FacebookLoginPage.fxml"));

            Parent root = loader.load();

            FacebookLoginPageController controller = loader.getController();
            controller.setMainApp(this);

//            String css = this.getClass().getResource("/MainMenu.css").toExternalForm();
            Scene JumbleScene = new Scene(root);
//            JumbleScene.getStylesheets().add(css);

            stage.setTitle("All Jumbled Up - Facebook Login");
            stage.setScene(JumbleScene);
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showHomeScene() {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/HomeScene.fxml"));

            Parent root = loader.load();

            HomeSceneController controller = loader.getController();
            controller.setMainApp(this);

//            String css = this.getClass().getResource("/MainMenu.css").toExternalForm();
            Scene JumbleScene = new Scene(root);
//            JumbleScene.getStylesheets().add(css);

            stage.setTitle("All Jumbled Up - Welcome!");
            stage.setScene(JumbleScene);
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Ends the Game */
    public void gameOver() {
        stage.close();
    }

}
