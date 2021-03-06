package AllJumbledUp;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by enea.
 * Date: 10/27/15.
 * Time: 2:39 AM.
 */

public class DbManager {

    private static Logger logger = Logger.getLogger("AllJumbledUp");

    /**
     * The Mongod reference.
     */
    private static MongoDatabase db;

    /**
     * Key riddle pair.
     */
    private static ArrayList<String> KeyRiddle = new ArrayList<String>(2);

    public DbManager(String dbName) {
        MongoClient mongoClient = new MongoClient();
        db = mongoClient.getDatabase(dbName);
    }

    /**
     * Connect to db and initialize collections.
     * @return: true if the connection is succesfull.
     */
    public static boolean connect() {
        try {
            initDB();
        } catch (MongoTimeoutException e) {
            logger.log(Level.WARNING, "\nMongod process is probably not running. Trying to start mongod process.. \n");
            return false;
        }
        return true;
    }

    /**
     * Attempts to open mongod process when not found.
     * @return: true if process started sucessfully.
     */
    public static boolean startMongodProcess() {
        try {
            new ProcessBuilder("mongod").start();
        } catch (Exception p) {
            //alert.setHeaderText("Could not start Mondod process.. Please start Mongod process manually.");
            logger.log(Level.SEVERE, "Could not start mongod process", p);
            return false;
        }
        return true;
    }

    /**
     * Get Database.
     */
    public static MongoDatabase getDb(String dbName) throws UnknownHostException {
        MongoClient mongoClient = new MongoClient();

        if (db == null) {
            db = mongoClient.getDatabase(dbName);
        }
        return db;
    }

    /**
     * Import from file to collection.
     */
    public static void dbImport(String inputFilename, MongoCollection<Document> collection) {
        try {
            try (BufferedReader reader = new BufferedReader(new FileReader(inputFilename))) {
                String json;
                while ((json = reader.readLine()) != null) {
                    collection.insertOne(Document.parse(json));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clean all the collections data in mongod.
     */
    public static void cleanDB() {
        /* Clear jumbled words*/
        db.getCollection("jumbled_words").deleteMany(new Document());
        /* Clear key riddles */
        db.getCollection("key_riddles").deleteMany(new Document());
        db.getCollection("key_image_riddles").deleteMany(new Document());
    }

    /**
     * Init the DB: Import all jumbled words and final words-story pairs.
     */
    public static void initDB() {
        if (db.getCollection("jumbled_words").count() < 1) {
        /* Import jumbled words */
            File file = new File("src/main/resources/wordlist.txt");
            dbImport(file.getAbsolutePath(),
                    db.getCollection("jumbled_words"));
        }

        if (db.getCollection("key_riddles").count() < 1) {
        /* Import keys-riddles */
            File file = new File("src/main/resources/KeyRiddleList.txt");
            dbImport(file.getAbsolutePath(), db.getCollection("key_riddles"));
        }

        if (db.getCollection("key_image_riddles").count() < 1) {
        /* Import keys-riddles */
            File file = new File("src/main/resources/keyImageRiddles.txt");
            dbImport(file.getAbsolutePath(), db.getCollection("key_image_riddles"));
        }
    }

    /**
     * Insert user if not exists.
     */
    public static void manageUser () {
        switch (GameManager.getGameMode()) {
            case FreePlay:
                break;
            case FacebookUser:
                /* If user is not stored, insert him */
                if (db.getCollection("FB_users").find(new Document("_id", Session.getSessionID())).limit(1).first() ==
                        null)
                    db.getCollection("FB_users").insertOne(new Document()
                                    .append("_id", Session.getSessionID())
                                    .append("FullName", Session.getFullName())
//                                    .append("Scores", asList(
//                                                    new Document()
//                                                        .append("DateTime", "2015")
//                                                        .append("Score", 0))
//                                    )
                    );
                break;
            default:
        }
    }

    public static void saveScore (int score) {
        Date date = new Date();
        // Store Score and DateTime
        db.getCollection("FB_users").updateOne(new Document("_id", Session.getSessionID()),
                new Document("$push", new Document("Scores", new Document()
                        .append("Score", score)
                        .append("DateTime", date))));
    }

    /**
     * Returns the user's scores.
     * @return the user's scores.
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Document> getMyScoreHistory () {
        FindIterable<Document> player = db.getCollection("FB_users").find(new Document("_id",
                Session.getSessionID())).limit(1);

        if (player.first().containsKey("Scores")) {
            ArrayList<Document> scores = (ArrayList<Document>) player.first().get("Scores");
            Collections.sort(scores, (s1, s2) -> (int) s2.get("Score") - (int) s1.get("Score"));
            return scores;
        }
        return new ArrayList<>();
    }

    /**
     * Returns best score of top players.
     * @return best score of top players.
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Document> getPlayersHighscores () {
        ArrayList<Document> playersHighscores = new ArrayList<>();
        FindIterable<Document> players = db.getCollection("FB_users").find();

        players.forEach((Block<Document>) player -> {
            if (player.containsKey("Scores")) {
                ArrayList<Document> scores = (ArrayList<Document>) player.get("Scores");

                // Sort scores for this player - highest first
                Collections.sort(scores, (s1, s2) -> (int) s2.get("Score") - (int) s1.get("Score"));

                Document playerHighScore = new Document()
                        .append("FullName", player.get("FullName"))
                        .append("Score", scores.get(0).get("Score"))
                        .append("DateTime", scores.get(0).get("DateTime"));

                playersHighscores.add(playerHighScore);
            }
        });
        return playersHighscores;
    }

    /**
     * Log collection documents.
     */
    public static void printCollection(String collection) {
        FindIterable<Document> iterable = db.getCollection(collection).find();
        iterable.forEach((Block<Document>) System.out::println);
    }

    /**
     * Prints collection.
     * @param iterable of collection.
     */
    public static void printCollection(FindIterable<Document> iterable) {
        iterable.forEach((Block<Document>) System.out::println);
    }

    /**
     * Generates the final word and riddle pair.
     */
    public static ArrayList<String> generateFinalWordStoryPair() {
        int min, max, numOfLetters;

        switch (GameManager.getDifficultyLevel()) {
            case EASY:
                min = 4;
                max = 5;
                break;
            case MEDIUM:
                min = 6;
                max = 8;
                break;
            case HARD:
                min = 8;
                max = 11;
                break;
            default:
                min = 4;
                max = 5;
        }

        Random rn = new Random();
        numOfLetters = rn.nextInt(max - min + 1) + min;

        FindIterable<Document> iterableKR;

        String collection = "";
        switch (GameManager.getRiddleType()) {
            case TEXT:
                collection = "key_riddles";
                break;
            case IMAGE:
                collection = "key_image_riddles";
                break;
            default:
                collection = "key_image_riddles";
        }

        int wordLength = numOfLetters;
        // Checking from max to zero
        do {
            /* Sort by usage */
            iterableKR = db.getCollection(collection)
                .find(new BasicDBObject("$where", "this.key.length==" + wordLength))
                .sort(new Document("timesUsed", 1));
            wordLength --;
        } while (iterableKR.first() == null && wordLength > 0);

        wordLength = numOfLetters;
        if (null == iterableKR.first()) {
            // Checking from max to zero
            do {
            /* Sort by usage */
                iterableKR = db.getCollection(collection)
                        .find(new BasicDBObject("$where", "this.key.length==" + wordLength))
                        .sort(new Document("timesUsed", 1));
                wordLength++;
            } while (iterableKR.first() == null && numOfLetters < 15);
        }

        String key = iterableKR.first().get("key").toString();
        String riddle ="";
        if (GameManager.getRiddleType() == GameManager.RiddleType.IMAGE)
            riddle = "/images/" + iterableKR.first().get("riddle").toString() + ".png";
        else
            riddle = iterableKR.first().get("riddle").toString();
        String timesUsed = iterableKR.first().get("timesUsed").toString();

        /* Update timesUsed */
        db.getCollection(collection).updateOne(new Document("key", key),
                new Document("$set", new Document("timesUsed", Integer.parseInt(timesUsed) + 1)));

        if (!KeyRiddle.isEmpty())
            KeyRiddle.clear();
        KeyRiddle.add(0, key);
        KeyRiddle.add(1, riddle);

        return KeyRiddle;
    }

    /**
     * Generates the final word and riddle pair.
     */
    public static ArrayList<String> getFinalWordStoryPair() {
        return KeyRiddle;
    }

    /**
     * Generates the proper jumbled words - Implementation of jumbled words
     * selection algorithm.
     * Todo: check what happens if no words are found for the desired length
     */
    public static ArrayList<ArrayList<String>> getJumbledWords () {
        // the proper words that contain characters of the final word
        ArrayList<ArrayList<String>> jumbled_words = new ArrayList<>();

        /* Final word */
        String FW = KeyRiddle.get(0);

        int BucketSize = FW.length() / 4;
        int offsetBuckets = FW.length() % 4;
        int Buckets = 4 - offsetBuckets;
        int OffsetBucketSize = 0;
        if (offsetBuckets > 0)
            OffsetBucketSize = (FW.length() - BucketSize*Buckets) / offsetBuckets;

        /* Length of words according to difficulty level */
        int min, max, numOfLetters;
        switch (GameManager.getDifficultyLevel()) {
            case EASY:
                min = 4;
                max = 6;
                break;
            case MEDIUM:
                min = 7;
                max = 9;
                break;
            case HARD:
                min = 9;
                max = 12;
                break;
            default:
                min = 4;
                max = 6;
        }

        /* Iterate character buckets that have BucketSize length */
        for (int word = 0; word < 4; word++) {
            String specialChars = "";

            /* Initialize query for this bucket */
            BasicDBList conditionsList = new BasicDBList();

            Random rn = new Random();
            numOfLetters = rn.nextInt(max - min + 1) + min;

            if (word < Buckets) {
                /* Starting index of bucket */
                int pos = word*BucketSize;

                /* Iterate characters in bucket */
                for (int c = pos; c < FW.length() && c < pos + BucketSize; c++) {
                    specialChars += FW.charAt(c);
                    /* Query regex */
                    String regex = "[" + Character.toString(Character.toUpperCase(FW.charAt(c))) +
                            Character.toString(Character.toLowerCase(FW.charAt(c))) + "]";
                    /* Populate the conditions list */
                    conditionsList.add(new BasicDBObject("word",
                            java.util.regex.Pattern.compile(regex)));
                }
            }
            else {
                /* Starting index of bucket */
                int pos = Buckets * BucketSize + OffsetBucketSize * (word - Buckets);

                /* Iterate characters in offset bucket */
                for (int c = pos; c < FW.length() && c < pos + OffsetBucketSize; c++) {
                    specialChars += FW.charAt(c);
                    /* Query regex */
                    String regex = "[" + Character.toString(Character.toUpperCase(FW.charAt(c))) +
                            Character.toString(Character.toLowerCase(FW.charAt(c))) + "]";
                    /* Populate the conditions list */
                    conditionsList.add(new BasicDBObject("word",
                            java.util.regex.Pattern.compile(regex)));
                }
            }

            FindIterable<Document> it;
            BasicDBList conditions = new BasicDBList();

            do {
                conditions.clear();
                conditionsList.forEach(conditions::add);
                conditions.add(new BasicDBObject("$where", "this.word.length==" + numOfLetters));

                /* The query */
                BasicDBObject query = new BasicDBObject("$and", conditions);

                /* Get all proper jumbled word sorted by timesUsed */
                it = db.getCollection("jumbled_words").
                        find(query).sort(new Document("timesUsed", 1));

                numOfLetters --;
            } while (it.first() == null && numOfLetters > 0);

            logger.log(Level.INFO, "Bucket: " + conditions.toString());

            String jumbledWord = it.first().get("word").toString();
            String timesUsed = it.first().get("timesUsed").toString();

            /* Update timesUsed */
            db.getCollection("jumbled_words").updateOne(new Document("word", jumbledWord),
                    new Document("$set", new Document("timesUsed", Integer.parseInt(timesUsed) + 1)));

            ArrayList<String> inner = new ArrayList<>();
            inner.add(jumbledWord.toLowerCase());
            inner.add(specialChars.toLowerCase());
            jumbled_words.add(inner);
        }
        logger.log(Level.INFO, jumbled_words.toString());
        return jumbled_words;
    }
}
