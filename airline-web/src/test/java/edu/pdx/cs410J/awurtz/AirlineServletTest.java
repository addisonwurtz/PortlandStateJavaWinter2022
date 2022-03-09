package edu.pdx.cs410J.awurtz;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;

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
  void addOneFlightToAirline() throws IOException {
    AirlineServlet servlet = new AirlineServlet();

    String airlineName = "Test Airlines";
    int flightNumber = 123;
    String source = "MSP";

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAMETER)).thenReturn(airlineName);
    when(request.getParameter(AirlineServlet.FLIGHT_NUMBER_PARAMETER)).thenReturn(String.valueOf(flightNumber));
    when(request.getParameter(AirlineServlet.FLIGHT_SOURCE_PARAMETER)).thenReturn(source);

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
