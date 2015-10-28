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
import java.util.Arrays;
import java.util.List;

/**
 * Created by enea on 10/27/15.
 */
public class DbManager {
    private static MongoDatabase db;

    public  DbManager (String dbName) {
        MongoClient mongoClient = new MongoClient();
        db = mongoClient.getDatabase(dbName);
        initDB();
    }

    /* Get Database */
    public static MongoDatabase getDb (String dbName) throws UnknownHostException {

        MongoClient mongoClient = new MongoClient();

        if (db == null) {
            db = mongoClient.getDatabase(dbName);
        }
        return db;
    }

    public static void dbImport (String inputFilename, MongoCollection<Document> collection) {

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

    /* Init the DB: Import all jumbled words and final words-story pairs */
    public static void initDB () {

        /* Clear jumbled words*/
        db.getCollection("jumbled_words").deleteMany(new Document());
//        if (db.getCollection("jumbled_words").count() < 1)
            dbImport("/Users/enea/Dev/Villanova/AllJumbledUp/src/main/resources/wordlist.txt",
                    db.getCollection("jumbled_words"));

        /* Sort by usage */
        FindIterable<Document> iterableJW = db.getCollection("jumbled_words").find().sort(new Document("timesUsed", 1));
        iterableJW.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document);
            }
        });

        printCollection("jumbled_words");


        /* Clear jumbled words*/
        db.getCollection("key_riddles").deleteMany(new Document());
        dbImport("/Users/enea/Dev/Villanova/AllJumbledUp/src/main/resources/KeyRiddleList.txt",
                db.getCollection("key_riddles"));
        getFinalWordStoryPair();
//        dbImport("", db.getCollection("jumbled_words"));
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

    public static ArrayList<String> getFinalWordStoryPair () {

        ArrayList<String> key_riddle = new ArrayList<String>(2);

        /* Sort by usage */
        FindIterable<Document> iterableKR = db.getCollection("key_riddles").find().sort(new Document("timesUsed", 1));

        key_riddle.add(0, iterableKR.first().get("key").toString());
        key_riddle.add(1, iterableKR.first().get("riddle").toString());

//        iterableKR.forEach(new Block<Document>() {
//            @Override
//            public void apply(final Document document) {
//                System.out.println(document);
//            }
//        });

        return key_riddle;
    }







    //
//    public static ArrayList<String> getJumbledWords () {
//
//        return new ArrayList<>([{"dsf"}]);
//    }

}
