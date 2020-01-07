package io.github.komyagin;

import java.io.IOException;
import io.github.komyagin.dao.VocabSelection;
import org.json.simple.parser.*;
/**
 *
 * @author Semyon Komyagin
 */
public class KindleExport {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ParseException {
        new VocabSelection().selectAll();
    }
}