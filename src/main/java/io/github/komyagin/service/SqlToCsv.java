package io.github.komyagin.service;

import java.io.FileWriter;
import java.io.IOException;

public class SqlToCsv {
    // exporting to csv file

    public static void writeSqlToCsv() {
        FileWriter csvWriter;
        try {
            csvWriter = new FileWriter("new.csv");
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
