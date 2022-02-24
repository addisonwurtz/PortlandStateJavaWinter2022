package edu.pdx.cs410J.awurtz;

import edu.pdx.cs410J.ParserException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Converter class converts airline text files into airline XML files
 */
public class Converter {
    final String textFile;
    final String xmlFile;

    public Converter(String textFile, String xmlFile) {
        this.textFile = textFile;
        this.xmlFile = xmlFile;
    }

    public static void main(String[] args) {
        Converter converter = new Converter(args[0], args[1]);

        try {
            TextParser parser = new TextParser(new FileReader(String.valueOf(converter.textFile)));
            Airline airline = parser.parse();

            XmlDumper dumper = new XmlDumper(new FileWriter(converter.xmlFile));
            dumper.dump(airline);

            System.exit(0);


        } catch (FileNotFoundException e) {
            System.out.println("File " + converter.textFile + " was not found.");
            System.exit(1);
        } catch (ParserException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Could not write to xml file.");
            System.exit(1);
        }
    }
}
