package edu.pdx.cs410J.awurtz;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TextParserTest {

  @Test
  void validTextFileCanBeParsed() throws ParserException {
    InputStream resource = getClass().getResourceAsStream("valid-airline.txt");
    assertThat(resource, notNullValue());

    TextParser parser = new TextParser(new InputStreamReader(resource));
    Airline airline = parser.parse();
    assertThat(airline.getName(), equalTo("Test Airline"));
  }

  @Test
  void invalidTextFileThrowsParserException() {
    InputStream resource = getClass().getResourceAsStream("empty-airline.txt");
    assertThat(resource, notNullValue());

    TextParser parser = new TextParser(new InputStreamReader(resource));
    assertThrows(ParserException.class, parser::parse);
  }

  @Test
  void flightCanBeParsed() throws ParserException {
    InputStream resource = getClass().getResourceAsStream("valid-airline.txt");
    assertThat(resource, notNullValue());

    Flight flight = new Flight(99, "PDX", "01/24/2022", "11:11", "SFO", "1/25/2022", "12:12");
    TextParser parser = new TextParser(new InputStreamReader(resource));
    Airline airline = parser.parse();

    ArrayList<Flight> flights = (ArrayList<Flight>) airline.getFlights();
    assertThat(flights.get(0).toString(), containsString(flight.toString()));
  }
}
