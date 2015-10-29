package AllJumbledUp;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import static com.mongodb.client.model.Filters.*;


/**
 * Created by enea on 10/27/15.
 */
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
            BufferedReader reader = new BufferedReader(new FileReader(inputFilename));
            try {
                String json;
                while ((json = reader.readLine()) != null) {
                    collection.insertOne(Document.parse(json));
                }
            } finally {
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO: Remove clear
    /* Init the DB: Import all jumbled words and final words-story pairs */
    public static void initDB() {

        /* Clear jumbled words*/
        db.getCollection("jumbled_words").deleteMany(new Document());
//        if (db.getCollection("jumbled_words").count() < 1)
        /* Import jumbled words */
            dbImport("/Users/enea/Dev/Villanova/AllJumbledUp/src/main/resources/wordlist.txt",
                    db.getCollection("jumbled_words"));

        /* Sort by usage */
        FindIterable<Document> iterableJW = db.getCollection("jumbled_words").find().sort(new Document("timesUsed", 1));
//        printCollection("jumbled_words");

        /* Clear jumbled words */
        db.getCollection("key_riddles").deleteMany(new Document());
        /* Import keys-riddles */
        dbImport("/Users/enea/Dev/Villanova/AllJumbledUp/src/main/resources/KeyRiddleList.txt",
                db.getCollection("key_riddles"));
    }

    /* Log collection documents */
    public static void printCollection(String collection) {
        FindIterable<Document> iterable = db.getCollection(collection).find();
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document);
            }
        });
    }

    public static void printCollection(FindIterable<Document> iterable) {
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document);
            }
        });
    }

    //TODO: Add some randomness
    /* The Final word and story pair are produced here */
    public static ArrayList<String> generateFinalWordStoryPair() {
        /* Sort by usage */
        FindIterable<Document> iterableKR = db.getCollection("key_riddles").find().sort(new Document("timesUsed", 1));
        String key = iterableKR.first().get("key").toString();
        String riddle = iterableKR.first().get("riddle").toString();
        String timesUsed = iterableKR.first().get("timesUsed").toString();

        /* Update timesUsed */
        db.getCollection("key_riddles").updateOne(new Document("key", key),
                new Document("$set", new Document("timesUsed", Integer.parseInt(timesUsed) + 1)));

        KeyRiddle.add(0, key);
        KeyRiddle.add(1, riddle);

        return KeyRiddle;
    }

    /* The Final word and story pair are produced here */
    public static ArrayList<String> getFinalWordStoryPair() {
        return KeyRiddle;
    }

    /* Generate the proper jumbled words */
    public static ArrayList<String> getJumbledWords () {
        // the proper words that contain characters of the final word
        ArrayList<String> jumbled_words = new ArrayList<>();
        ArrayList<Character> FWchars = new ArrayList<>();

        /* Final word */
        String FW = KeyRiddle.get(0);
//        String FW = "Building";

        int BucketSize = FW.length() / 4;
        int offsetBuckets = FW.length() % 4;
        int Buckets = 4 - offsetBuckets;
        int OffsetBucketSize = 0;
        if (offsetBuckets > 0)
            OffsetBucketSize = (FW.length() - BucketSize*Buckets) / offsetBuckets;

        /* Iterate character buckets that have BucketSize length */
        for (int word = 0; word < 4; word++) {
            /* Clean characters in bucket */
            FWchars.clear();

            /* Initialize query for this bucket*/
            BasicDBList conditionsList = new BasicDBList();

            if (word < Buckets) {
                /* Starting index of bucket */
                int pos = word*BucketSize;

                /* Iterate characters in bucket */
                for (int c = pos; c < FW.length() && c < pos + BucketSize; c++) {
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
                    /* Query regex */
                    String regex = "[" + Character.toString(Character.toUpperCase(FW.charAt(c))) +
                            Character.toString(Character.toLowerCase(FW.charAt(c))) + "]";
                    /* Populate the conditions list */
                    conditionsList.add(new BasicDBObject("word",
                            java.util.regex.Pattern.compile(regex)));
                }
            }

            System.out.println("Bucket: " + conditionsList.toString());

            /* The query */
            BasicDBObject query = new BasicDBObject("$and", conditionsList);
            /* Get all proper jumbled word sorted by timesUsed */
            FindIterable<Document> it = db.getCollection("jumbled_words").
                    find(query).sort(new Document("timesUsed", 1));

            String jumbledWord = it.first().get("word").toString();
            String timesUsed = it.first().get("timesUsed").toString();

            /* Update timesUsed */
            db.getCollection("jumbled_words").updateOne(new Document("word", jumbledWord),
                    new Document("$set", new Document("timesUsed", Integer.parseInt(timesUsed) + 1)));

            jumbled_words.add(jumbledWord);

        }
//        System.out.println(jumbled_words);
        return jumbled_words;
    }

}
