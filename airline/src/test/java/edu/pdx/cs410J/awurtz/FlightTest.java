package edu.pdx.cs410J.awurtz;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link Flight} class.
 *
 * You'll need to update these unit tests as you build out you program.
 */
public class FlightTest {

  /**
   * This unit test will need to be modified (likely deleted) as you implement
   * your project.
   */
  @Test
  void getArrivalStringReturnsArriveDateAndArriveTimeAsString() {
    Flight flight = createTestFlight();

    assertThat(flight.getArrivalString(), is(flight.arriveTime + " " + flight.arriveDate));
  }

  /**
   * This unit test will need to be modified (likely deleted) as you implement
   * your project.
   */
  @Test
  void initiallyAllFlightsHaveTheSameNumber() {
    Flight flight = new Flight();
    assertThat(flight.getNumber(), equalTo(42));
  }

  @Test
  void forProject1ItIsOkayIfGetDepartureTimeReturnsNull() {
    Flight flight = new Flight();
    assertThat(flight.getDeparture(), is(nullValue()));
  }

  private static Flight createTestFlight() {
    String source = "PDX";
    Integer flightNumber = 36;
    String departDate = "02/09/2022";
    String departTime = "12:12";
    String destination = "SFO";
    String arrivalDate = "2/9/2022";
    String arrivalTime = "13:13";

    return new Flight(source, flightNumber, departDate, departTime, destination, arrivalDate, arrivalTime);
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

    assertThat(testFlight.dest, is(destination));
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
}
