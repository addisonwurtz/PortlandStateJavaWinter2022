package edu.pdx.cs410J.awurtz;

import edu.pdx.cs410J.AirlineParser;
import edu.pdx.cs410J.ParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

import static edu.pdx.cs410J.awurtz.Project2.parseArgsAndCreateFlight;
import static java.lang.Integer.parseInt;

/**
 * Parses contents of text file and creates an airline with associated flights using information from file.
 */
public class TextParser implements AirlineParser<Airline> {
  private final Reader reader;

  public TextParser(Reader reader) {
    this.reader = reader;
  }

  @Override
  public Airline parse() throws ParserException {
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
          throw new ParserException("Missing flight number");
        }

        String source = br.readLine();
        if (source == null) {
          throw new ParserException("Missing flight source.");
        }

        String departDate = br.readLine();
        if (departDate == null) {
          throw new ParserException("Missing departure date.");
        }

        String departTime = br.readLine();
        if (departTime == null) {
          throw new ParserException("Missing departure time.");
        }

        String destination = br.readLine();
        if (destination == null) {
          throw new ParserException("Missing flight destination.");
        }

        String arriveDate = br.readLine();
        if (arriveDate == null) {
          throw new ParserException("Missing arrival date.");
        }

        String arriveTime = br.readLine();
        if (arriveTime == null) {
          throw new ParserException("Missing arrival time.");
        }

        String[] flightArgs = {airlineName, flightNumber, source, departDate, departTime, destination, arriveDate, arriveTime};
        Flight flight = parseArgsAndCreateFlight(flightArgs);
        airline.addFlight(flight);
      } while (Objects.equals(br.readLine(), "***"));
      return airline;

    } catch (IOException ex) {
      throw new ParserException("Airline information could not be read from file.");
    }
  }
}



