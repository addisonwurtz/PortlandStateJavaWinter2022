package edu.pdx.cs410J.awurtz;

import edu.pdx.cs410J.ParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextParser {
  private final Reader reader;

  public TextParser(Reader reader) {
    this.reader = reader;
  }

  public Airline parse() throws ParserException {
   
    try (
      BufferedReader br = new BufferedReader(this.reader)
    ) {
      Airline airline = null;
      for (String line = br.readLine(); line != null; line = br.readLine()) {
        if(airline == null) {
          String airlineName = line;
          airline = new Airline(airlineName);
        } else
          airline.addFlight(new Flight(Integer.parseInt(line)));
      }
      return airline;

    } catch (IOException e) {
      throw new ParserException("While parsing airline", e);
    }

  }
}
