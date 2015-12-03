package AllJumbledUp;

import controllers.FacebookLoginPageController;
import controllers.GameSceneController;
import controllers.HomeSceneController;
import controllers.MenuController;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.auth.AccessToken;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GameManager extends Application {

    public static enum GameMode {
        FacebookUser, FreePlay
    }

    public static enum RiddleType {
        TEXT, IMAGE
    }

    public static enum ExitFlag {
        TIME_UP, SOLVED_RIDDLE, EXIT_GAME
    }

    public enum DifficultyLevel {
        EASY, MEDIUM, HARD
    }

    public static Facebook facebook;
    public static Session session;
    private static GameMode gameMode;
    private static RiddleType riddleType;
    private static int Score = 0;

    // Game sounds
    private static MediaPlayer mediaPlayer;
    private static HashMap <String, AudioClip> sounds = new HashMap<>();
    private static HashMap <String, Media> musicClips = new HashMap<>();

    /* Game Settings */
    private static DifficultyLevel difficultyLevel = DifficultyLevel.EASY;
    private static boolean backgroundMusic = true;
    private static boolean fxSounds = true;
    private static int timer; //Timer in seconds
    private static final double WINDOW_MIN_HEIGHT = 600;
    private static final double WINDOW_MIN_WIDTH = 850;

    /* Words Dictionaries */
    private List <JumbledWord> JumbledWords = new ArrayList<>();
    private static String JW, Story;

    /* Game Stage */
    public Stage stage;

    /*Constructor */
    public GameManager() {
        new DbManager("AllJumbledUp");
        // db.cleanDB();
        session = new Session();
        loadMusicClips();
        loadFxSounds();
    }

    /* Shuffle the list of jumbled words and return it */
    public List<JumbledWord> getJumbledWords() {
        long seed = System.nanoTime();
        Collections.shuffle(JumbledWords, new Random(seed));
        return JumbledWords;
    }

    public static String getJW() {
        return JW;
    }

    public static String getStory() {
        return Story;
    }

    public static DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public boolean getBackgroundMusic() {
        return backgroundMusic;
    }

    public boolean getFxSounds() {
        return fxSounds;
    }

    public static GameMode getGameMode () {
        return gameMode;
    }

    public static RiddleType getRiddleType () {
        return riddleType;
    }

    public int getTimer(){
        return timer;
    }

    public int getScore() {
        return Score;
    }

    public void setBackgroundMusic(boolean isSoundOn) {
        backgroundMusic = isSoundOn;
        if (!backgroundMusic) {
            mediaPlayer.setMute(true);
        } else
            mediaPlayer.setMute(false);
    }

    public void setFxSounds(boolean isSoundOn) {
        fxSounds = isSoundOn;
        if (!fxSounds) {
            sounds.clear();
        } else
            loadFxSounds();
    }

    public void setDifficultyLevelLevel(DifficultyLevel lvl) {
        difficultyLevel = lvl;
    }

    private void setTimer() {
        switch (difficultyLevel) {
            case EASY:
                timer = 240;
                break;
            case MEDIUM:
                timer = 210;
                break;
            case HARD:
                timer = 180;
                break;
            default:
                timer = 240;
        }
    }

    public void setGameMode (GameMode gm) {
        gameMode = gm;
    }

    public void setRiddleType (RiddleType rt) {
        System.out.println("Riddle type: " + rt);
        riddleType = rt;
    }

    public static void setFBOAuthAccessToken (AccessToken at) {
        facebook.setOAuthAccessToken(at);
    }

    /* Assign Final Word */
    public void assignFW(ArrayList<String> key_pair) {
        JW = key_pair.get(0);
        Story = key_pair.get(1);
        System.out.println("FW: " + JW + " Riddle: " + Story);
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

    public String updateTimer() {
        timer --;
        String m = Integer.toString(timer / 60);
        m = (m.length() < 2) ? "0" + m : m ;
        String s = Integer.toString(timer % 60);
        s = (s.length() < 2) ? "0" + s : s ;
        return "Time: " + m + ":" + s;
    }

    /* Final score when the riddle is found */
    public void updateScore(int lettersFound, int timeRemaining, boolean SolvedRiddle) {
        int pointsPerLetter = 5;
        int baseScore;
        int pointsPerTimeRemaining = 5;

        if (!SolvedRiddle) {
            Score = lettersFound * pointsPerLetter;
        }
        else {
            switch (difficultyLevel) {
                case EASY:
                    baseScore = 100;
                    break;
                case MEDIUM:
                    baseScore = 300;
                    break;
                case HARD:
                    baseScore = 500;
                    break;
                default:
                    baseScore = 100;
            }
            Score += baseScore + timeRemaining * pointsPerTimeRemaining;
        }
    }

    public void saveScore() {
        DbManager.saveScore(getScore());
    }

    public void postScoreOnFacebook() {
        try {
            String msg =
                    "...............(0 0)\n" +
                            ".---oOO--- (_)-----.\n" +
                            "╔════════════════╗\n" +
                            "║               "+getScore()+" \n" +
                            "╚════════════════╝\n" +
                            "'----------------------oOO\n" +
                            ".............|__|__|\n" +
                            "............... || ||\n" +
                            ".......... ooO Ooo";

            facebook.postStatusMessage("Yeah!!! My new high score in GameManager is ...\n\n" + msg);
            System.out.println("Yeah!!! My new high score in GameManager is ...\n\n" + msg);

        } catch (FacebookException e1) {
            e1.printStackTrace();
        }
    }

    public void loadFxSounds() {
        sounds.put("keyPressed", new AudioClip(getClass().getResource("/sounds/2.wav").toString()));
        sounds.put("foundWord", new AudioClip(getClass().getResource("/sounds/3.wav").toString()));
        sounds.put("unlockedFW", new AudioClip(getClass().getResource("/sounds/4.mp3").toString()));
        sounds.put("invalidChar", new AudioClip(getClass().getResource("/sounds/1.wav").toString()));
    }

    public void loadMusicClips () {
        musicClips.put("background1", new Media(getClass().getResource("/sounds/HotlineBling.mp3").toString()));
        musicClips.put("start", new Media(getClass().getResource("/sounds/ambient-loop_1.mp3").toString()));
        musicClips.put("GameOn1", new Media(getClass().getResource("/sounds/countdown.wav").toString()));
        musicClips.put("GameOn2", new Media(getClass().getResource("/sounds/groove.wav").toString()));
    }

    public static void playFxSound (String fxSound) {
        if (null != sounds.get(fxSound))
            sounds.get(fxSound).play();
    }

    public static void playMusicClip (String musicClip) {
        if (null != musicClips.get(musicClip)) {
            if (null != mediaPlayer)
                mediaPlayer.stop();
            mediaPlayer = new MediaPlayer(musicClips.get(musicClip));
            mediaPlayer.play();
            mediaPlayer.setCycleCount(Timeline.INDEFINITE);
        }
    }

    public void showMainMenuScene() {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/MainMenu.fxml"));

            Parent root = loader.load();

            MenuController controller = loader.getController();
            controller.setMainApp(this);

            String css = this.getClass().getResource("/css/MainMenu.css").toExternalForm();
            Scene JumbleScene = new Scene(root);
            JumbleScene.getStylesheets().add(css);

            stage.setTitle("All Jumbled Up - Main Menu");
            stage.setScene(JumbleScene);
            stage.setResizable(true);
            stage.setMinHeight(WINDOW_MIN_HEIGHT);
            stage.setMinWidth(WINDOW_MIN_WIDTH);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMainGameScene() {
        try {

            FXMLLoader loader = new FXMLLoader();//(getClass().getResource("GameManager.fxml"));
            loader.setLocation(getClass().getResource("/fxml/AllJumbledUp.fxml"));

            Parent root = loader.load();
            GameSceneController gameSceneController = loader.getController();
            gameSceneController.setMainApp(this);

            String css = this.getClass().getResource("/css/AllJumbledUp.css").toExternalForm();
            Scene JumbleScene = new Scene(root);
            JumbleScene.getStylesheets().add(css);

            stage.setTitle("All Jumbled Up");
            stage.setScene(JumbleScene);
            stage.setResizable(true);
            stage.setMinHeight(WINDOW_MIN_HEIGHT);
            stage.setMinWidth(WINDOW_MIN_WIDTH);
            stage.show();

            if (backgroundMusic)
                playMusicClip("GameOn2");

            JumbleScene.getWindow().setOnCloseRequest(ev -> {
                if (!gameSceneController.shutdown()) {
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
            loader.setLocation(getClass().getResource("/fxml/FacebookLoginPage.fxml"));

            Parent root = loader.load();

            FacebookLoginPageController controller = loader.getController();
            controller.setMainApp(this);

//            String css = this.getClass().getResource("/MainMenu.css").toExternalForm();
            Scene JumbleScene = new Scene(root);
//            JumbleScene.getStylesheets().add(css);

            stage.setTitle("All Jumbled Up - Facebook Login");
            stage.setScene(JumbleScene);
            stage.setResizable(true);
            stage.setMinHeight(WINDOW_MIN_HEIGHT);
            stage.setMinWidth(WINDOW_MIN_WIDTH);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showHomeScene() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/HomeScene.fxml"));

            Parent root = loader.load();

            HomeSceneController controller = loader.getController();
            controller.setMainApp(this);

            String css = this.getClass().getResource("/css/HomeScene.css").toExternalForm();
            Scene JumbleScene = new Scene(root);
            JumbleScene.getStylesheets().add(css);

            stage.setTitle("All Jumbled Up - Welcome!");
            stage.setScene(JumbleScene);
            stage.setResizable(true);
            stage.setMinHeight(WINDOW_MIN_HEIGHT);
            stage.setMinWidth(WINDOW_MIN_WIDTH);
            stage.show();

            if (backgroundMusic)
                playMusicClip("start");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Ends the Game */
    public void gameOver() {
        stage.close();
    }

    public static void startSession () {
        try {
            session = new Session(facebook.getMe().getId(), facebook.getMe().getName(),
                    facebook.getPictureURL(500, 500).toString());
            //todo: check manage user
            DbManager.manageUser();
            System.out.println(GameManager.session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Game Starts/Resets here */
    public void startGame() {
        Score = 0;
        setRiddleType((ThreadLocalRandom.current().nextInt(0, 1 + 1) == 0) ? RiddleType.TEXT : RiddleType.IMAGE);
        DbManager.generateFinalWordStoryPair();
        assignFW(DbManager.getFinalWordStoryPair());
        assignJumbledWords(DbManager.getJumbledWords());
        setTimer();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        showHomeScene();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
