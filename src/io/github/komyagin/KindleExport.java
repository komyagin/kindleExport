package io.github.komyagin;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

// for working with Kindle vocab.db
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// for working with external JSON dictionary
import org.json.simple.JSONObject;
import org.json.simple.parser.*;


/**
 *
 * @author Semyon Komyagin
 */

public class KindleExport {

    /**
     * Connect to the vocab.db database
     * @return the Connection object
     */
    private Connection connect() {
        // SQLite connection string
        //TODO: need to implement Kindle's path autodetection
        String url = "jdbc:sqlite:/home/trwne/Documents/vocab.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }


    /**
     * select from vocab.db
     */

    public void selectAll() throws IOException, ParseException {

        // create a JSON's dictionary object
        //TODO: implement some different dictionary
        Object obj = new JSONParser().parse(new FileReader("dictionary.json"));
        JSONObject jo = (JSONObject) obj;

        // exporting to csv file
        FileWriter csvWriter = new FileWriter("new.csv");

        String sql = "SELECT\n" +
                "    word_key,\n" +
                "    usage,\n" +
                "    LOOKUPS.timestamp,\n" +
                "    WORDS.word,\n" +
                "    WORDS.stem,\n" +
                "    LOOKUPS.dict_key,\n" +
                "    BOOK_INFO.title AS book_title\n" +
                "FROM LOOKUPS\n" +
                "         INNER JOIN WORDS on WORDS.id=LOOKUPS.word_key\n" +
                "         INNER JOIN BOOK_INFO on LOOKUPS.book_key=BOOK_INFO.id";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            System.out.println("--------------------------------------------------");
            // loop through the result set
            while (rs.next()) {
                if(jo.get(rs.getString("word")) != null) {
                    System.out.println("WORD: " + rs.getString("word") + "\t" +
                            "STEM: " + rs.getString("stem") + "\t" +
                            "DEFINITION: " + (String) jo.get(rs.getString("stem")));
                    System.out.println("--------------------------------------------------");
                    csvWriter.append(rs.getString("word"));
                    csvWriter.append(";");
                    csvWriter.append((String) jo.get(rs.getString("word")));
                    csvWriter.append("\n");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ParseException {
        KindleExport app = new KindleExport();
        app.selectAll();
    }

}