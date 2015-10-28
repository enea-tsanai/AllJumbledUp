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
        printCollection("jumbled_words");

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

        int numOfCharsPerJword = FW.length() / 4;
        int numOfoffsetWords = FW.length() % 4;

        /* Iterate character buckets that have numOfCharsPerJword length */
        for (int word = 0; word < FW.length() - numOfoffsetWords; word++) {
            /* Clean characters in bucket */
            FWchars.clear();

            /* Starting index of bucket */
            int pos = word*numOfCharsPerJword;

            /* Iterate characters in bucket */
            for (int c = pos; c < pos+numOfCharsPerJword-1; c++ ) {
                FWchars.add(FW.charAt(c));
                ;
            }
        }
        return jumbled_words;
    }





}
