package edu.pdx.cs410J.awurtz;

import edu.pdx.cs410J.ParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextParser {
  private final Reader reader;

  public TextParser(Reader reader) {
    this.reader = reader;
  }

  public Airline parse() throws ParserException {
   
//    try (
//      BufferedReader br = new BufferedReader(this.reader)
//    ) {
//      Airline airline = null;
//      for (String line = br.readLine(); line != null; line = br.readLine()) {
//        if(airline == null) {
//          String airlineName = line;
//          airline = new Airline(airlineName);
//        } else
//          //TODO update parser to parse all flight args
//          airline.addFlight(new Flight(Integer.parseInt(line)));
//      }
//      return airline;
//
//    } catch (IOException e) {
//      throw new ParserException("While parsing airline", e);
//    }

    try (
            BufferedReader br = new BufferedReader(this.reader)
    ) {

      String airlineName = br.readLine();
      if (airlineName == null) {
        throw new ParserException("Missing airline name");
      }

      Airline airline = new Airline(airlineName);

      do {
        String flightNumber = br.readLine();
        if (flightNumber == null) {
          throw new ParserException("Flight number missing from file.");
        }

        String source = br.readLine();
        if (source == null) {
          throw new ParserException("Flight source missing from file.");
        }

        String departDate = br.readLine();
        if (departDate == null) {
          throw new ParserException("Departure date missing from surgery.");
        }


        String departTime = br.readLine();
        if (departTime == null) {
          throw new ParserException("Departure time missing from file.");
        }

        String departAmPm = br.readLine();
        if (departAmPm == null) {
          throw new ParserException("Departure am/pm missing from file");
        }

        String destination = br.readLine();
        if (destination == null) {
          throw new ParserException("Flight destination missing from file.");
        }

        String arriveDate = br.readLine();
        if (arriveDate == null) {
          throw new ParserException("Arrival date missing from file.");
        }

        String arriveTime = br.readLine();
        if (arriveTime == null) {
          throw new ParserException("Arrival time missing from file.");
        }

        String arriveAmpPm = br.readLine();
        if(arriveAmpPm == null) {
          throw new ParserException("Arrival am/pm missing from file.");
        }

        String[] flightArgs = {airlineName, flightNumber, source, departDate, departTime, departAmPm, destination, arriveDate, arriveTime, arriveAmpPm};
        Flight flight = new Flight(flightArgs);
        airline.addFlight(flight);
      } while (Objects.equals(br.readLine(), "***"));
      return airline;

    } catch (IOException ex) {
      throw new ParserException("Airline information could not be read from file.");
    }
  }
}
