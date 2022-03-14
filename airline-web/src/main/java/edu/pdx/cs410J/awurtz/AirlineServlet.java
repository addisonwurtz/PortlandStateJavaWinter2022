package edu.pdx.cs410J.awurtz;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.web.HttpRequestHelper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * This servlet ultimately provides a REST API for working with an
 * <code>Airline</code>.  However, in its current state, it is an example
 * of how to use HTTP and Java servlets to store simple dictionary of words
 * and their definitions.
 */
public class AirlineServlet extends HttpServlet {
  static final String AIRLINE_NAME_PARAMETER = "airline";
  static final String FLIGHT_NUMBER_PARAMETER = "flightNumber";
  static final String FLIGHT_SOURCE_PARAMETER = "source";
  static final String FLIGHT_DEPART_PARAMETER = "depart";
  static final String FLIGHT_DEST_PARAMETER = "dest";
  static final String FLIGHT_ARRIVE_PARAMETER = "arrive";

  private final Map<String, Airline> airlines = new HashMap<>();

  /**
   * Handles an HTTP GET request from a client by writing the definition of the
   * airline specified in the "airline" HTTP parameter to the HTTP response.  If the
   * "flight" parameter is not specified, all the flights in the airline
   * are written to the HTTP response.
   */
  @Override
  protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws IOException
  {

      response.setContentType("application/xml");

      int parameterCount = getHttpRequestParameterCount(request);

      if(parameterCount == 0) {
          missingRequiredParameter(response, AIRLINE_NAME_PARAMETER);
      }
      if(parameterCount == 1) {

          String airlineName = getParameter(AIRLINE_NAME_PARAMETER, request);
          if (airlineName != null) {
              dumpAirline(airlineName, response);

          } else {
              missingRequiredParameter(response, AIRLINE_NAME_PARAMETER);
          }
      } else if (parameterCount == 3) {

          String airlineName = getParameter(AIRLINE_NAME_PARAMETER, request);
          if (airlineName == null) {
              missingRequiredParameter(response, AIRLINE_NAME_PARAMETER);
          }

          String flightSource = getParameter(FLIGHT_SOURCE_PARAMETER, request);
          String flightDestination = getParameter(FLIGHT_DEST_PARAMETER, request);
          //validate parameters
          try {
              flightSource = Flight.parseAirportCode(flightSource);
              if (flightSource == null) {
                  missingRequiredParameter(response, flightSource);
              }

              flightDestination = Flight.parseAirportCode(flightDestination);
              if (flightDestination == null) {
                  missingRequiredParameter(response, flightDestination);
              }
          } catch (InvalidAirportCodeException ex) {
              String message = ex.getInvalidAirportCode() + " is not a valid airport code.";
              response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
          }

          searchFlightsAndDump(airlineName, flightSource, flightDestination, response);
      }
  }

  /**
   * Handles an HTTP POST request by storing the dictionary entry for the
   * "word" and "definition" request parameters.  It writes the dictionary
   * entry to the HTTP response.
   */
  @Override
  protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
      String airlineName = getParameter(AIRLINE_NAME_PARAMETER, request );
      String flightNumberString = getParameter(FLIGHT_NUMBER_PARAMETER, request );
      String source = getParameter(FLIGHT_SOURCE_PARAMETER, request);
      String departure = getParameter(FLIGHT_DEPART_PARAMETER, request);
      String destination = getParameter(FLIGHT_DEST_PARAMETER, request);
      String arrival = getParameter(FLIGHT_ARRIVE_PARAMETER, request);

      try {
      if (airlineName == null) {
          missingRequiredParameter(response, AIRLINE_NAME_PARAMETER);
          return;
      }

      if ( flightNumberString == null) {
          missingRequiredParameter( response, FLIGHT_NUMBER_PARAMETER);
          return;
      }

      if (source == null) {
          missingRequiredParameter(response, FLIGHT_SOURCE_PARAMETER);
          return;
      }
      source = Flight.parseAirportCode(source);

      if(departure == null) {
          missingRequiredParameter(response, FLIGHT_DEPART_PARAMETER);
      }
      departure = Flight.parseDateTime(departure);

      if(destination == null) {
          missingRequiredParameter(response, FLIGHT_DEST_PARAMETER);
      }
      destination = Flight.parseAirportCode(destination);


      if(arrival == null) {
          missingRequiredParameter(response, FLIGHT_ARRIVE_PARAMETER);
      }
      arrival = Flight.parseDateTime(arrival);

      Airline airline = getOrCreateAirline(airlineName);

          airline.addFlight(new Flight(Integer.parseInt(flightNumberString), source, departure, destination, arrival));
      } catch (NumberFormatException ex) {
          String message = flightNumberString + " is not a valid flight number.";
          response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
      } catch (InvalidAirportCodeException ex) {
          String message = ex.getInvalidAirportCode() + " is not a valid airport code.";
          response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
      } catch (InvalidDateException ex) {
          String message = ex.getInvalidDate() + " is not a valid date.";
          response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
      }

      response.setStatus( HttpServletResponse.SC_OK);
  }

  /**
   * Handles an HTTP DELETE request by removing all dictionary entries.  This
   * behavior is exposed for testing purposes only.  It's probably not
   * something that you'd want a real application to expose.
   */
  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
      response.setContentType("text/plain");

      this.airlines.clear();

      PrintWriter pw = response.getWriter();
      pw.println(Messages.allDictionaryEntriesDeleted());
      pw.flush();

      response.setStatus(HttpServletResponse.SC_OK);

  }

  /**
   * Writes an error message about a missing parameter to the HTTP response.
   *
   * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
   */
  private void missingRequiredParameter( HttpServletResponse response, String parameterName )
      throws IOException
  {
      String message = Messages.missingRequiredParameter(parameterName);
      response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
  }

  /**
   * Writes the definition of the given airline to the HTTP response.
   *
   * The text of the message is formatted with {@link TextDumper}
   */
  private void dumpAirline(String airlineName, HttpServletResponse response) throws IOException {

    Airline airline = getAirline(airlineName);

    if (airline == null) {
        String message = airlineName + " was not found.";
        response.sendError(HttpServletResponse.SC_NOT_FOUND, message);

    } else {
      PrintWriter pw = response.getWriter();

      XmlDumper dumper = new XmlDumper(pw);
      dumper.dump(airline);

      response.setStatus(HttpServletResponse.SC_OK);
    }
  }

  private void searchFlightsAndDump(String airlineName, String source, String destination, HttpServletResponse response) throws IOException {
      Airline airline = getAirline(airlineName);

      if(airline == null) {
          response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      }
      else {
          Airline airlineWithMatchingFlights = new Airline(airlineName);

          Collection<Flight> flights = airline.getFlights();
          for (Flight flight : flights) {
              if (flight.getSource().equals(source) && flight.getDestination().equals(destination)) {
                  airlineWithMatchingFlights.addFlight(flight);
              }
          }
          PrintWriter pw = response.getWriter();

          XmlDumper dumper = new XmlDumper(pw);

           if(airlineWithMatchingFlights.getFlights().size() > 0) {
              dumper.dump(airlineWithMatchingFlights);
          } else {
               response.setStatus(HttpServletResponse.SC_NO_CONTENT);
           }


          response.setStatus(HttpServletResponse.SC_OK);
      }
  }

    /**
   * Returns the value of the HTTP request parameter with the given name.
   *
   * @return <code>null</code> if the value of the parameter is
   *         <code>null</code> or is the empty string
   */
  private String getParameter(String name, HttpServletRequest request) {
    String value = request.getParameter(name);
    if (value == null || "".equals(value)) {
      return null;

    } else {
      return value;
    }
  }

  public int getHttpRequestParameterCount(HttpServletRequest request) {
      int i = 0;

      if (getParameter(AIRLINE_NAME_PARAMETER, request) != null) {
          ++i;
      }
      if (getParameter(FLIGHT_NUMBER_PARAMETER, request) != null) {
          ++i;
      }
      if(getParameter(FLIGHT_SOURCE_PARAMETER, request) != null) {
          ++i;
      }
      if(getParameter(FLIGHT_DEPART_PARAMETER, request) != null) {
          ++i;
      }
      if(getParameter(FLIGHT_DEST_PARAMETER, request) != null) {
          ++i;
      }
      if(getParameter(FLIGHT_ARRIVE_PARAMETER, request) != null) {
          ++i;
      }

      return i;
  }


  @VisibleForTesting
  Airline getOrCreateAirline(String airlineName) {
      Airline airline = this.airlines.get(airlineName);
      if( airline == null) {
          airline = new Airline(airlineName);
          this.airlines.put(airlineName, airline);
      }
      return airline;
  }

  @VisibleForTesting
  Airline getAirline(String airlineName) {
      return this.airlines.get(airlineName);
  }

}
