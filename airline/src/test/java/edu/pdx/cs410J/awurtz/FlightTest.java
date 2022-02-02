package edu.pdx.cs410J.awurtz;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
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
    assertThat(flight.getDepartureString(), is("2/9/22 12:12 PM"));
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
    String departTime = "12:12";
    String destination = "SFO";
    String arrivalDate = "2/9/2022";
    String arrivalTime = "13:13";

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
    String departTime = "12:12";
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
    String arrivalTime = "13:13";
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
    SimpleDateFormat stringToDate = new SimpleDateFormat("MM/dd/yyyy k:mm");

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
    SimpleDateFormat stringToDate = new SimpleDateFormat("MM/dd/yyyy k:mm");

    Date date = stringToDate.parse(dateString);
    assertThat(flight.getArrival(), equalTo(date));
  }

  @Test
  void getArrivalThrowsInvalidArrivalExceptionForInvalidDateString() {
    Flight flight = new Flight();
    assertThrows(InvalidArrivalException.class, flight::getArrival);
  }
}

