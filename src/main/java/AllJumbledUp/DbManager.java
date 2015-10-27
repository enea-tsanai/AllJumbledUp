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
        
        FindIterable<Document> iterable = db.getCollection("jumbled_words").find().sort(new Document("timesUsed", 1));


        FindIterable<Document> iterable = db.getCollection("jumbled_words").find();
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document);
            }
        });
//        dbImport("", db.getCollection("jumbled_words"));
    }
//
//    public static ArrayList<String> getJumbledWords () {
//
//        return new ArrayList<>([{"dsf"}]);
//    }

    public static String getFinalWordStoryPair () {
        return "test";
    }

}
