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

import static java.lang.Integer.parseInt;

public class TextParser {
  private final Reader reader;

  public TextParser(Reader reader) {
    this.reader = reader;
  }

  public Airline parse() throws ParserException {

    String flightNumber = null;
    Airline airline = null;

    try (
            BufferedReader br = new BufferedReader(this.reader)
    ) {

      String airlineName = br.readLine();
      if (airlineName == null) {
        throw new ParserException("Missing airline name");
      }

      airline = new Airline(airlineName);

      do {
        flightNumber = br.readLine();
        if (flightNumber == null) {
          throw new ParserException("Flight number missing from flight.");
        }

        String source = br.readLine();
        if (source == null) {
          throw new ParserException("Flight source missing from flight.");
        }

        String departure = br.readLine();
        if (departure == null) {
          throw new ParserException("Departure date missing from flight.");
        }

        String destination = br.readLine();
        if (destination == null) {
          throw new ParserException("Flight destination missing from flight.");
        }

        String arrival = br.readLine();
        if (arrival == null) {
          throw new ParserException("Arrival date missing from flight.");
        }

        Flight flight = new Flight(parseInt(flightNumber), source, departure, destination, arrival);
        airline.addFlight(flight);

      } while (Objects.equals(br.readLine(), "***"));
      return airline;

    } catch (IOException e) {
      throw new ParserException("While parsing airline", e);
    } catch (NumberFormatException exception) {
      System.err.println(flightNumber + " is not a valid integer value.");
      System.exit(1);
    }
    assert(airline != null);
    return airline;
  }
}
