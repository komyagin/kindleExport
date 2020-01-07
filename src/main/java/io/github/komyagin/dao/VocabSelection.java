package io.github.komyagin.dao;

import io.github.komyagin.util.ConnectionFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class VocabSelection {
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

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            System.out.println("--------------------------------------------------");
            // loop through the result set
            while (rs.next()) {
                if(jo.get(rs.getString("word")) != null) {
                    System.out.println("WORD: " + rs.getString("word") + "\t" +
                            "STEM: " + rs.getString("stem") + "\t" +
                            "DEFINITION: " + jo.get(rs.getString("stem")));
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
}
