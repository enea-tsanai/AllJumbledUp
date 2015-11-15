package AllJumbledUp;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by enea.
 * Date: 10/27/15.
 * Time: 2:39 AM.
 */

//TODO: implement users and sessions
//TODO: Check dictionary for existing words with 11, 12 letters etc.
public class DbManager {

    private static MongoDatabase db;

    /* Key riddle pair */
    private static ArrayList<String> KeyRiddle = new ArrayList<String>(2);


    public  DbManager(String dbName) {
        MongoClient mongoClient = new MongoClient();
        db = mongoClient.getDatabase(dbName);
        initDB();
    }

    /* Get Database */
    public static MongoDatabase getDb(String dbName) throws UnknownHostException {
        MongoClient mongoClient = new MongoClient();

        if (db == null) {
            db = mongoClient.getDatabase(dbName);
        }
        return db;
    }

    /* Import from file to collection */
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

    public static void cleanDB() {
        /* Clear jumbled words*/
        db.getCollection("jumbled_words").deleteMany(new Document());
        /* Clear key riddles */
        db.getCollection("key_riddles").deleteMany(new Document());
    }

    /* Init the DB: Import all jumbled words and final words-story pairs */
    public static void initDB() {
        if (db.getCollection("jumbled_words").count() < 1) {
        /* Import jumbled words */
            dbImport("/Users/enea/Dev/Villanova/AllJumbledUp/src/main/resources/wordlist.txt",
                    db.getCollection("jumbled_words"));
        }

        if (db.getCollection("key_riddles").count() < 1) {
        /* Import keys-riddles */
            dbImport("/Users/enea/Dev/Villanova/AllJumbledUp/src/main/resources/KeyRiddleList.txt",
                    db.getCollection("key_riddles"));
        }
    }

    /* Log collection documents */
    public static void printCollection(String collection) {
        FindIterable<Document> iterable = db.getCollection(collection).find();
        iterable.forEach((Block<Document>) System.out::println);
    }

    public static void printCollection(FindIterable<Document> iterable) {
        iterable.forEach((Block<Document>) System.out::println);
    }

    //TODO: Add some randomness
    /* The Final word and story pair are produced here */
    public static ArrayList<String> generateFinalWordStoryPair() {
        int min, max, numOfLetters;

        switch (AllJumbledUp.getDifficultyLevel()) {
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

        do {
            /* Sort by usage */
            iterableKR = db.getCollection("key_riddles")
                .find(new BasicDBObject("$where", "this.key.length==" + numOfLetters))
                .sort(new Document("timesUsed", 1));
            numOfLetters --;
        } while (iterableKR.first() == null && numOfLetters > 0);

        String key = iterableKR.first().get("key").toString();
        String riddle = iterableKR.first().get("riddle").toString();
        String timesUsed = iterableKR.first().get("timesUsed").toString();

        /* Update timesUsed */
        db.getCollection("key_riddles").updateOne(new Document("key", key),
                new Document("$set", new Document("timesUsed", Integer.parseInt(timesUsed) + 1)));

        if (!KeyRiddle.isEmpty())
            KeyRiddle.clear();
        KeyRiddle.add(0, key);
        KeyRiddle.add(1, riddle);

        return KeyRiddle;
    }

    /* The Final word and story pair are produced here */
    public static ArrayList<String> getFinalWordStoryPair() {
        return KeyRiddle;
    }

    /* Generate the proper jumbled words
     * Todo: check what happens if no words are found for the desired length */
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
        switch (AllJumbledUp.getDifficultyLevel()) {
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

            System.out.println("Bucket: " + conditions.toString());

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
        System.out.println(jumbled_words);
        return jumbled_words;
    }
}
