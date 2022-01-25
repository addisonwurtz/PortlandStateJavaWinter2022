package edu.pdx.cs410J.awurtz;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class TextDumperTest {

  @Test
  void airlineNameIsDumpedInTextFormat() {
    String airlineName = "Test Airline";
    Airline airline = new Airline(airlineName);

    StringWriter sw = new StringWriter();
    TextDumper dumper = new TextDumper(sw);
    dumper.dump(airline);

    String text = sw.toString();
    assertThat(text, containsString(airlineName));
  }


  @Test
  void canParseTextWrittenByTextDumper(@TempDir File tempDir) throws IOException, ParserException {
    String airlineName = "Test Airline\n99\nLAX\n12/12/2021\n15:45\nDEN\n12/15/2021\n11:11";
    Airline airline = new Airline(airlineName);

    File textFile = new File(tempDir, "airline.txt");
    TextDumper dumper = new TextDumper(new FileWriter(textFile));
    dumper.dump(airline);

    TextParser parser = new TextParser(new FileReader(textFile));
    Airline read = parser.parse();
    assertThat(read.getName(), containsString("Test Airline"));
  }

  @Test
  void FlightNumberIsDumpedInTextFormat() {
    String airlineName = "Delta";
    Airline airline = new Airline(airlineName);
    Flight flight = new Flight(12, "PDX", "01/24/2022", "11:11", "SFO", "01/24/2022", "12:12");

    airline.addFlight(flight);

    StringWriter sw = new StringWriter();
    TextDumper dumper = new TextDumper(sw);
    dumper.dump(airline);
    String text = sw.toString();

    assertThat(text, containsString(String.valueOf(flight.getNumber())));
  }

  @Test
  void FlightSourceIsDumpedInTextFormat() {
    String airlineName = "Delta";
    Airline airline = new Airline(airlineName);
    Flight flight = new Flight(12, "PDX", "01/24/2022", "11:11", "SFO", "01/24/2022", "12:12");

    airline.addFlight(flight);

    StringWriter sw = new StringWriter();
    TextDumper dumper = new TextDumper(sw);
    dumper.dump(airline);
    String text = sw.toString();

    assertThat(text, containsString(flight.getSource()));

  }

  @Test
  void FlightDepartDateIsDumpedInTextFormat() {
    String airlineName = "Delta";
    Airline airline = new Airline(airlineName);
    Flight flight = new Flight(12, "PDX", "01/24/2022", "11:11", "SFO", "01/24/2022", "12:12");

    airline.addFlight(flight);

    StringWriter sw = new StringWriter();
    TextDumper dumper = new TextDumper(sw);
    dumper.dump(airline);
    String text = sw.toString();

    assertThat(text, containsString(flight.getDepartDate()));

  }

  @Test
  void FlightDepartTimeIsDumpedInTextFormat() {
    String airlineName = "Delta";
    Airline airline = new Airline(airlineName);
    Flight flight = new Flight(12, "PDX", "01/24/2022", "11:11", "SFO", "01/24/2022", "12:12");

    airline.addFlight(flight);

    StringWriter sw = new StringWriter();
    TextDumper dumper = new TextDumper(sw);
    dumper.dump(airline);
    String text = sw.toString();

    assertThat(text, containsString(flight.getDepartTime()));

  }

  @Test
  void FlightDestinationIsDumpedInTextFormat() {
    String airlineName = "Delta";
    Airline airline = new Airline(airlineName);
    Flight flight = new Flight(12, "PDX", "01/24/2022", "11:11", "SFO", "01/24/2022", "12:12");

    airline.addFlight(flight);

    StringWriter sw = new StringWriter();
    TextDumper dumper = new TextDumper(sw);
    dumper.dump(airline);
    String text = sw.toString();

    assertThat(text, containsString(flight.getDestination()));
  }

  @Test
  void FlightArriveDateIsDumpedInTextFormat() {
    String airlineName = "Delta";
    Airline airline = new Airline(airlineName);
    Flight flight = new Flight(12, "PDX", "01/24/2022", "11:11", "SFO", "01/25/2022", "12:12");

    airline.addFlight(flight);

    StringWriter sw = new StringWriter();
    TextDumper dumper = new TextDumper(sw);
    dumper.dump(airline);
    String text = sw.toString();

    assertThat(text, containsString(flight.getArriveDate()));
  }

  @Test
  void FlightArriveTimeIsDumpedInTextFormat() {
    String airlineName = "Delta";
    Airline airline = new Airline(airlineName);
    Flight flight = new Flight(12, "PDX", "01/24/2022", "11:11", "SFO", "01/25/2022", "12:12");

    airline.addFlight(flight);

    StringWriter sw = new StringWriter();
    TextDumper dumper = new TextDumper(sw);
    dumper.dump(airline);
    String text = sw.toString();

    assertThat(text, containsString(flight.getArriveTime()));
  }

  @Test
  void InformationForMultipleFlightsIsDumpedInTextFormat() {
    String airlineName = "Delta";
    Airline airline = new Airline(airlineName);
    Flight flight1 = new Flight(12, "PDX", "01/24/2022", "11:11", "SFO", "01/25/2022", "12:12");
    Flight flight2 = new Flight(13, "LAX", "01/24/2022", "11:11", "MSP", "01/25/2022", "15:12");

    airline.addFlight(flight1);
    airline.addFlight(flight2);

    StringWriter sw = new StringWriter();
    TextDumper dumper = new TextDumper(sw);
    dumper.dump(airline);
    String text = sw.toString();

    assertThat(text, containsString(flight1.getSource()));
    assertThat(text, containsString(flight2.getSource()));
    assertThat(text, containsString(flight1.getArriveTime()));
    assertThat(text, containsString(flight2.getArriveTime()));
  }
}
