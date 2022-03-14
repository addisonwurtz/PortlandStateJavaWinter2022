package edu.pdx.cs410J.awurtz;

import edu.pdx.cs410J.ParserException;
import org.apache.groovy.json.internal.JsonParserLax;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.metal.MetalScrollPaneUI;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static edu.pdx.cs410J.awurtz.AirlineServlet.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * A unit test for the {@link AirlineServlet}.  It uses mockito to
 * provide mock http requests and responses.
 */
class AirlineServletTest {

  @Test
  void missingAirlineNameReturnsPreconditionFailedStatus() throws IOException {
    AirlineServlet servlet = new AirlineServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);

    servlet.doGet(request, response);

    verify(response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED,
            Messages.missingRequiredParameter(AirlineServlet.AIRLINE_NAME_PARAMETER));
  }

  @Test
  void dumpingAirlineThatIsNotInServerThrowsRestException() throws IOException {
    String airlineName = "New Airline";
    AirlineServlet servlet = new AirlineServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);
    when(request.getParameter(AIRLINE_NAME_PARAMETER)).thenReturn(airlineName);

    servlet.doGet(request, response);

    verify(response).sendError(HttpServletResponse.SC_NOT_FOUND,  airlineName + " was not found.");

  }

  @Test
  void addOneFlightToAirline() throws IOException, ParseException {
    SimpleDateFormat dateFormat = new SimpleDateFormat(Flight.dateTimePattern);
    AirlineServlet servlet = new AirlineServlet();

    String airlineName = "Test Airlines";
    int flightNumber = 123;
    String source = "MSP";
    Date departure = dateFormat.parse("03/08/2022 5:11 pm");
    String destination = "SFO";
    Date arrival = dateFormat.parse("03/08/2022 11:30 pm");

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAMETER)).thenReturn(airlineName);
    when(request.getParameter(AirlineServlet.FLIGHT_NUMBER_PARAMETER)).thenReturn(String.valueOf(flightNumber));
    when(request.getParameter(AirlineServlet.FLIGHT_SOURCE_PARAMETER)).thenReturn(source);
    when(request.getParameter(AirlineServlet.FLIGHT_DEPART_PARAMETER)).thenReturn(dateFormat.format(departure));
    when(request.getParameter(AirlineServlet.FLIGHT_DEST_PARAMETER)).thenReturn(destination);
    when(request.getParameter(AirlineServlet.FLIGHT_ARRIVE_PARAMETER)).thenReturn(dateFormat.format(arrival));

    HttpServletResponse response = mock(HttpServletResponse.class);

    servlet.doPost(request, response);

    // Use an ArgumentCaptor when you want to make multiple assertions against the value passed to the mock
    //ArgumentCaptor<Integer> statusCode = ArgumentCaptor.forClass(Integer.class);
    verify(response).setStatus(eq(HttpServletResponse.SC_OK));

    Airline airline = servlet.getOrCreateAirline(airlineName);
    assertThat(airline, notNullValue());
    Collection<Flight> flights = airline.getFlights();
    assertThat(flights, hasSize(1));
    Flight flight = flights.iterator().next();
    assertThat(flight.getNumber() , equalTo(flightNumber));
    assertThat(flight.getSource(), equalTo(source));
    assertThat(flight.getDeparture(), equalTo(departure));
    assertThat(flight.getDestination(), equalTo(destination));
    assertThat(flight.getArrival(), equalTo(arrival));
  }

  @Test
  void returnXmlRepresentationOfAirline() throws IOException, ParserException {
    String airlineName = "Test Airlines";
    int flightNumber = 123;

    AirlineServlet servlet = new AirlineServlet();
    Airline airline1 = servlet.getOrCreateAirline(airlineName);
    airline1.addFlight(new Flight(flightNumber, "PDX"));

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAMETER)).thenReturn(airlineName);

    HttpServletResponse response = mock(HttpServletResponse.class);
    StringWriter sw = new StringWriter();
    when(response.getWriter()).thenReturn(new PrintWriter(sw, true));

    servlet.doGet(request, response);

    verify(response).setStatus(eq(HttpServletResponse.SC_OK));

    String text = sw.toString();
    Airline airline2 = new XmlParser(text).parse();

    assertThat(airline2.getName(), equalTo(airlineName));
    Collection<Flight> flights = airline2.getFlights();
    assertThat(flights, hasSize(1));
    Flight flight = flights.iterator().next();
    assertThat(flight.getNumber() , equalTo(flightNumber));
  }

  @Test
  void returnFlightsThatMatchSearchSrcAndDest() throws ParseException, IOException, ParserException {
    SimpleDateFormat dateFormat = new SimpleDateFormat(Flight.dateTimePattern);

    String airlineName = "Delta";
    int flightNumber = 123;
    String source = "MSP";
    Date departure = dateFormat.parse("03/08/2022 5:11 pm");
    String destination = "SFO";
    Date arrival = dateFormat.parse("03/08/2022 11:30 pm");

    AirlineServlet servlet = new AirlineServlet();
    Airline airline = servlet.getOrCreateAirline(airlineName);
    airline.addFlight(new Flight(flightNumber, source, dateFormat.format(departure), destination, dateFormat.format(arrival)));
    airline.addFlight(new Flight(666, "LAX", "01/01/2023", "12:00 am", "SFO",
            "01/01/2023", "2:00 am"));
    airline.addFlight(new Flight(111, source, dateFormat.format(departure), "LAX",
            dateFormat.format(arrival)));
    airline.addFlight(new Flight(789, source, dateFormat.format(departure), destination, dateFormat.format(arrival)));

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAMETER)).thenReturn(airlineName);
    when(request.getParameter(AirlineServlet.FLIGHT_SOURCE_PARAMETER)).thenReturn(source);
    when(request.getParameter(AirlineServlet.FLIGHT_DEST_PARAMETER)).thenReturn(destination);

    HttpServletResponse response = mock(HttpServletResponse.class);
    StringWriter sw = new StringWriter();
    when(response.getWriter()).thenReturn(new PrintWriter(sw, true));

    servlet.doGet(request, response);

    verify(response).setStatus(eq(HttpServletResponse.SC_OK));

    String text = sw.toString();
    Airline airline2 = new XmlParser(text).parse();

    Collection<Flight> flights = airline2.getFlights();
    assertThat(flights.size(), equalTo(2));
    for (Flight flight: flights) {
      assertThat(flight.getSource(), equalTo(source));
      assertThat(flight.getDestination(), equalTo(destination));
    }
  }

  @Test
  void SearchWithNoMatchingFLightsHasNoContentStatus() throws IOException {
    String airlineName = "Delta";
    String source = "MSP";
    String destination = "SFO";

    AirlineServlet servlet = new AirlineServlet();

    Airline airline = servlet.getOrCreateAirline(airlineName);
    airline.addFlight(new Flight(1234, "PDX", "01/01/2021 11:11 am", "LAX",
            "01/01/2021 12:12 pm"));

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);

    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAMETER)).thenReturn(airlineName);
    when(request.getParameter(AirlineServlet.FLIGHT_SOURCE_PARAMETER)).thenReturn(source);
    when(request.getParameter(AirlineServlet.FLIGHT_DEST_PARAMETER)).thenReturn(destination);

    servlet.doGet(request, response);

    verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
  }


  @Test
  void testGetHttpParameterCountReturns0WithNoParameters() {
    AirlineServlet servlet = new AirlineServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);

    assertThat(servlet.getHttpRequestParameterCount(request), equalTo(0));
  }

  @Test
  void testGetHttpParameterCountReturns3ForMockSearchRequest() {
    String airlineName = "Delta";
    String source = "MSP";
    String destination = "SFO";

    AirlineServlet servlet = new AirlineServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAMETER)).thenReturn(airlineName);
    when(request.getParameter(AirlineServlet.FLIGHT_SOURCE_PARAMETER)).thenReturn(source);
    when(request.getParameter(AirlineServlet.FLIGHT_DEST_PARAMETER)).thenReturn(destination);

    assertThat(servlet.getHttpRequestParameterCount(request), equalTo(3));
  }

  @Test
  void doGetReturnsPreconditionFailedStatusForInvalidSourceAirport() throws IOException {
    String airlineName = "Delta";
    String source = "AAA";
    String destination = "SFO";

    AirlineServlet servlet = new AirlineServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAMETER)).thenReturn(airlineName);
    when(request.getParameter(AirlineServlet.FLIGHT_SOURCE_PARAMETER)).thenReturn(source);
    when(request.getParameter(AirlineServlet.FLIGHT_DEST_PARAMETER)).thenReturn(destination);

    HttpServletResponse response = mock(HttpServletResponse.class);

    servlet.doGet(request, response);
    verify(response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED, source + " is not a valid airport code.");
  }

  @Test
  void doGetReturnsPreconditionFailedStatusForInvalidDestinationAirport() throws IOException {
    String airlineName = "Delta";
    String source = "SFO";
    String destination = "123";

    AirlineServlet servlet = new AirlineServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAMETER)).thenReturn(airlineName);
    when(request.getParameter(AirlineServlet.FLIGHT_SOURCE_PARAMETER)).thenReturn(source);
    when(request.getParameter(AirlineServlet.FLIGHT_DEST_PARAMETER)).thenReturn(destination);

    HttpServletResponse response = mock(HttpServletResponse.class);

    servlet.doGet(request, response);
    verify(response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED, destination + " is not a valid airport code.");
  }

  @Test
  void doPostReturnsPreconditionFailedStatusForInvalidFlightNumber() throws IOException {
    String airlineName = "Delta";
    String flightNumber = "12z";
    String source = "MSP";
    String departure = "03/08/2022 5:11 pm";
    String destination = "SFO";
    String arrival = "03/08/2022 11:30 pm";

    AirlineServlet servlet = new AirlineServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);

    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAMETER)).thenReturn(airlineName);
    when(request.getParameter(AirlineServlet.FLIGHT_NUMBER_PARAMETER)).thenReturn(flightNumber);
    when(request.getParameter(AirlineServlet.FLIGHT_SOURCE_PARAMETER)).thenReturn(source);
    when(request.getParameter(AirlineServlet.FLIGHT_DEPART_PARAMETER)).thenReturn(departure);
    when(request.getParameter(AirlineServlet.FLIGHT_DEST_PARAMETER)).thenReturn(destination);
    when(request.getParameter(AirlineServlet.FLIGHT_ARRIVE_PARAMETER)).thenReturn(arrival);

    HttpServletResponse response = mock(HttpServletResponse.class);

    servlet.doPost(request, response);
    verify(response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED, flightNumber + " is not a valid flight number.");
  }

  @Test
  void doPostReturnsPreconditionFailedStatusForInvalidSource() throws IOException {
    String airlineName = "Delta";
    String flightNumber = "122";
    String source = "MS5";
    String departure = "03/08/2022 5:11 pm";
    String destination = "SFO";
    String arrival = "03/08/2022 11:30 pm";

    AirlineServlet servlet = new AirlineServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);

    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAMETER)).thenReturn(airlineName);
    when(request.getParameter(AirlineServlet.FLIGHT_NUMBER_PARAMETER)).thenReturn(flightNumber);
    when(request.getParameter(AirlineServlet.FLIGHT_SOURCE_PARAMETER)).thenReturn(source);
    when(request.getParameter(AirlineServlet.FLIGHT_DEPART_PARAMETER)).thenReturn(departure);
    when(request.getParameter(AirlineServlet.FLIGHT_DEST_PARAMETER)).thenReturn(destination);
    when(request.getParameter(AirlineServlet.FLIGHT_ARRIVE_PARAMETER)).thenReturn(arrival);

    HttpServletResponse response = mock(HttpServletResponse.class);

    servlet.doPost(request, response);
    verify(response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED, source + " is not a valid airport code.");
  }

  @Test
  void doPostReturnsPreconditionFailedStatusForInvalidDestination() throws IOException {
    String airlineName = "Delta";
    String flightNumber = "122";
    String source = "MSp";
    String departure = "03/08/2022 5:11 pm";
    String destination = "(SFO)";
    String arrival = "03/08/2022 11:30 pm";

    AirlineServlet servlet = new AirlineServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);

    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAMETER)).thenReturn(airlineName);
    when(request.getParameter(AirlineServlet.FLIGHT_NUMBER_PARAMETER)).thenReturn(flightNumber);
    when(request.getParameter(AirlineServlet.FLIGHT_SOURCE_PARAMETER)).thenReturn(source);
    when(request.getParameter(AirlineServlet.FLIGHT_DEPART_PARAMETER)).thenReturn(departure);
    when(request.getParameter(AirlineServlet.FLIGHT_DEST_PARAMETER)).thenReturn(destination);
    when(request.getParameter(AirlineServlet.FLIGHT_ARRIVE_PARAMETER)).thenReturn(arrival);

    HttpServletResponse response = mock(HttpServletResponse.class);

    servlet.doPost(request, response);
    verify(response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED, destination + " is not a valid airport code.");
  }

  @Test
  void doPostReturnsPreconditionFailedStatusForInvalidDepartureDate() throws IOException {
    String airlineName = "Delta";
    String flightNumber = "122";
    String source = "MSP";
    String departure = "03/08/2022 13:11 pm";
    String destination = "SFO";
    String arrival = "03/08/2022 11:30 pm";

    AirlineServlet servlet = new AirlineServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);

    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAMETER)).thenReturn(airlineName);
    when(request.getParameter(AirlineServlet.FLIGHT_NUMBER_PARAMETER)).thenReturn(flightNumber);
    when(request.getParameter(AirlineServlet.FLIGHT_SOURCE_PARAMETER)).thenReturn(source);
    when(request.getParameter(AirlineServlet.FLIGHT_DEPART_PARAMETER)).thenReturn(departure);
    when(request.getParameter(AirlineServlet.FLIGHT_DEST_PARAMETER)).thenReturn(destination);
    when(request.getParameter(AirlineServlet.FLIGHT_ARRIVE_PARAMETER)).thenReturn(arrival);

    HttpServletResponse response = mock(HttpServletResponse.class);

    servlet.doPost(request, response);
    verify(response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED, departure + " is not a valid date.");
  }

  @Test
  void doPostReturnsPreconditionFailedStatusForInvalidArrivalDate() throws IOException {
    String airlineName = "Delta";
    String flightNumber = "122";
    String source = "MSP";
    String departure = "03/08/2022 3:11 pm";
    String destination = "SFO";
    String arrival = "03/08/222 11:30 pm";

    AirlineServlet servlet = new AirlineServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);

    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAMETER)).thenReturn(airlineName);
    when(request.getParameter(AirlineServlet.FLIGHT_NUMBER_PARAMETER)).thenReturn(flightNumber);
    when(request.getParameter(AirlineServlet.FLIGHT_SOURCE_PARAMETER)).thenReturn(source);
    when(request.getParameter(AirlineServlet.FLIGHT_DEPART_PARAMETER)).thenReturn(departure);
    when(request.getParameter(AirlineServlet.FLIGHT_DEST_PARAMETER)).thenReturn(destination);
    when(request.getParameter(AirlineServlet.FLIGHT_ARRIVE_PARAMETER)).thenReturn(arrival);

    HttpServletResponse response = mock(HttpServletResponse.class);

    servlet.doPost(request, response);
    verify(response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED, arrival + " is not a valid date.");
  }
}
