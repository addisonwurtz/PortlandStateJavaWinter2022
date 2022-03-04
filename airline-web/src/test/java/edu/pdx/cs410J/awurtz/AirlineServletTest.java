package edu.pdx.cs410J.awurtz;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

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

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAMETER)).thenReturn(airlineName);
    when(request.getParameter(AirlineServlet.FLIGHT_NUMBER_PARAMETER)).thenReturn(String.valueOf(flightNumber));

    HttpServletResponse response = mock(HttpServletResponse.class);

    servlet.doPost(request, response);

    // Use an ArgumentCaptor when you want to make multiple assertions against the value passed to the mock
    ArgumentCaptor<Integer> statusCode = ArgumentCaptor.forClass(Integer.class);
    verify(response).setStatus(statusCode.capture());

    assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_OK));

    Airline airline = servlet.getAirline(airlineName);
    assertThat(airline, notNullValue());
    Collection<Flight> flights = airline.getFlights();
    assertThat(flights, hasSize(1));
    Flight flight = flights.iterator().next();
    assertThat(flight.getNumber() , equalTo(flightNumber));
  }

}
