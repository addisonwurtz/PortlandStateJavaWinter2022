package edu.pdx.cs410J.awurtz;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link Flight} class.
 *
 * You'll need to update these unit tests as you build out you program.
 */
public class FlightTest {

  @Test
  void getArrivalStringReturnsArriveDateAndArriveTimeAsString() {
    Flight flight = createTestFlight();
    assertThat(flight.getArrivalString(), is("2/9/22 1:13 PM"));
  }

  @Test
  void getNumberReturnsFlightNumber() {
    Flight flight = createTestFlight();
    assertThat(flight.getNumber(), equalTo(flight.flightNumber));
  }

  @Test
  void getSourceReturnsSource() {
   Flight flight = createTestFlight();
   assertThat(flight.getSource(), equalTo(flight.source));
  }

  @Test
  void getDepartureStringReturnsDepartureString() {
    Flight flight = createTestFlight();
    assertThat(flight.getDepartureString(), is("2/9/22 12:12 AM"));
  }

  @Test
  void getDestinationReturnsDestination() {
    Flight flight = createTestFlight();
    assertThat(flight.getDestination(), equalTo(flight.destination));
  }

  private static Flight createTestFlight() {
    String source = "PDX";
    Integer flightNumber = 36;
    String departDate = "02/09/2022";
    String departTime = "12:12 am";
    String destination = "SFO";
    String arrivalDate = "2/9/2022";
    String arrivalTime = "1:13 pm";

    return new Flight(flightNumber, source, departDate, departTime, destination, arrivalDate, arrivalTime);
  }

  @Test
  void flightHasASourceAndFlightNumber() {
    String source = "PDX";
    Integer flightNumber = 36;
    Flight testFlight = FlightTest.createTestFlight();

    assertThat(testFlight.source, is(source));
    assertThat(testFlight.flightNumber, is(flightNumber));
  }

  @Test
  void flightHasDepartDate() {
    String departDate = "02/09/2022";
    Flight testFlight = FlightTest.createTestFlight();

    assertThat(testFlight.departDate, is(departDate));
  }

  @Test
  void flightHasDepartTime() {
    String departTime = "12:12 am";
    Flight testFlight = FlightTest.createTestFlight();

    assertThat(testFlight.departTime, is(departTime));
  }

  @Test
  void flightHasDestination() {
    String destination = "SFO";
    Flight testFlight = FlightTest.createTestFlight();

    assertThat(testFlight.destination, is(destination));
  }

  @Test
  void flightHasArrivalDate() {
    String arrivalDate = "2/9/2022";
    Flight testFlight = FlightTest.createTestFlight();

    assertThat(testFlight.arriveDate, is(arrivalDate));
  }

  @Test
  void flightHasArriveTime() {
    String arrivalTime = "1:13 pm";
    Flight testFlight = FlightTest.createTestFlight();

    assertThat(testFlight.arriveTime, is(arrivalTime));
  }

  @Test
  void getDepartDateReturnsDepartDate() {
    Flight flight = createTestFlight();
    assertThat(flight.getDepartDate(), equalTo(flight.departDate));
  }

  @Test
  void getDepartTimeReturnsDepartTime() {
    Flight flight = createTestFlight();
    assertThat(flight.getDepartTime(), equalTo(flight.departTime));
  }

  @Test
  void getArriveDateReturnsArriveDate() {
    Flight flight = createTestFlight();
    assertThat(flight.getArriveDate(), equalTo(flight.arriveDate));
  }

  @Test
  void getArriveTimeReturnsArriveTime() {
    Flight flight = createTestFlight();
    assertThat(flight.getArriveTime(), equalTo(flight.arriveTime));
  }

  @Test
  void getDepartureReturnsValidDateObject() throws ParseException {
    Flight flight = createTestFlight();
    String dateString = flight.getDepartDate() + " " + flight.getDepartTime();
    SimpleDateFormat stringToDate = new SimpleDateFormat("MM/dd/yyyy h:mm");

    Date date = stringToDate.parse(dateString);
    assertThat(flight.getDeparture(), equalTo(date));
  }

  @Test
  void getDepartureThrowsInvalidDepartureExceptionForInvalidDateString() {
    Flight flight = new Flight();
    assertThrows(InvalidDepartureException.class, flight::getDeparture);
  }

  @Test
  void getArrivalReturnsValidDateObject() throws ParseException {
    Flight flight = createTestFlight();
    String dateString = flight.getArriveDate() + " " + flight.getArriveTime();
    SimpleDateFormat stringToDate = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

    Date date = stringToDate.parse(dateString);
    assertThat(flight.getArrival(), equalTo(date));
  }

  @Test
  void getArrivalThrowsInvalidArrivalExceptionForInvalidDateString() {
    Flight flight = new Flight();
    assertThrows(InvalidArrivalException.class, flight::getArrival);
  }

  @Test
  void compareToReturnsZeroForDuplicateFlights() {
    Flight flight1 = createTestFlight();
    Flight flight2 = createTestFlight();

    assertThat(flight1.compareTo(flight2), equalTo(0));
  }

  @Test
  void compareToReturnsOneWhenComparingSourceDENToSourcePDX() {
    Flight flight1 = new Flight(456, "DEN", "2/1/2022", "6:53 pm", "LAX",
            "2/1/2022", "11:27 pm");
    Flight flight2 = createTestFlight();

    assertThat(flight1.compareTo(flight2), equalTo(-1));
  }

  @Test
  void compareToReturnsNegativeOneWhenComparingSourcePDXToSourceDEN() {
    Flight flight1 = createTestFlight();
    Flight flight2 = new Flight(456, "DEN", "2/1/2022", "6:53 pm", "LAX",
            "2/1/2022", "11:27 pm");

    assertThat(flight1.compareTo(flight2), equalTo(1));
  }

  @Test
  void compareToReturnsOneForFlightsWithSameSourceButFirstFlightDepartsEarlier() {
    Flight flight1 = new Flight(456, "DEN", "2/1/2022", "6:53 pm", "LAX",
            "2/1/2022", "11:27 pm");
    Flight flight2 = new Flight(456, "DEN", "3/1/2022", "6:53 pm", "LAX",
            "3/1/2022", "11:27 pm");

    assertThat(flight1.compareTo(flight2), equalTo(-1));
  }

  @Test
  void compareToReturnsNegative1ForFlightsWithSameSourceButSecondFlightDepartsEarlier() {
    Flight flight1 = new Flight(456, "DEN", "2/2/2022", "6:53 pm", "LAX",
            "2/2/2022", "11:27 pm");
    Flight flight2 = new Flight(456, "DEN", "2/1/2022", "6:53 pm", "LAX",
            "2/1/2022", "11:27 pm");

    assertThat(flight1.compareTo(flight2), equalTo(1));
  }

  @Test
  void flightConstructorThrowsExceptionWhenDepartureTimeIsAfterArrivalTime() {

    assertThrows(InvalidTimeException.class, () -> new Flight(456, "DEN", "3/1/2022",
            "6:53 pm", "LAX", "2/1/2022", "11:27 pm"));
  }

  @Test
  void compareAirportSourceCodes() {
    String source1 = "PDX";
    String source2 = "SEA";

    assertThat(source1.compareTo(source2), equalTo(-3));
  }

  @Test
  void parseAirportCodeReturnsValidCodeInUppercase() {
    String code = "msp";
    assertThat(Flight.parseAirportCode(code), equalTo(code.toUpperCase()));
  }
}

