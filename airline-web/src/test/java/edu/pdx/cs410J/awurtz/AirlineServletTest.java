package edu.pdx.cs410J.awurtz;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static edu.pdx.cs410J.awurtz.AirlineServlet.FLIGHT_SOURCE_PARAMETER;
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

    //when(request.getParameter(AirlineServlet.FLIGHT_NUMBER_PARAMETER)).thenReturn(String.valueOf(flightNumber));
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
}
