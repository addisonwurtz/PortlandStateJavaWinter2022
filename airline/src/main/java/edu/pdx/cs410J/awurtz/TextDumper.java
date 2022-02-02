package edu.pdx.cs410J.awurtz;

import edu.pdx.cs410J.AirlineDumper;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;


/**
 * Dumps contents of an airline (including flights) to a text file.
 */
public class TextDumper implements AirlineDumper<Airline> {
  private final Writer writer;

  public TextDumper(Writer writer) {
    this.writer = writer;
  }

  /**
   * Dumps airline and flight information into location specified by the writer.
   * @param airline object (including flight list)
   */
  @Override
  public void dump(Airline airline) {
    Boolean firstIteration = true;
    try (
      PrintWriter pw = new PrintWriter(this.writer)
      ) {
      pw.println(airline.getName());

      ArrayList<Flight> flightList = (ArrayList<Flight>) airline.getFlights();
      if(!flightList.isEmpty()) {
        for (Flight flight : flightList) {

          StringTokenizer departTimeTokenizer = new StringTokenizer(flight.getDepartTime(), " ");
          StringTokenizer arriveTimeTokenizer =  new StringTokenizer(flight.getArriveTime(), " ");
          if(!firstIteration) {
            pw.println("***");
          }
          else {
            firstIteration = false;
          }
            pw.println(flight.getNumber());
            pw.println(flight.getSource());
            pw.println(flight.getDepartDate());
          try {
            pw.println(departTimeTokenizer.nextToken());
            pw.println(departTimeTokenizer.nextToken());
          }
          catch (NoSuchElementException ex) {
            throw new InvalidTimeException("Departure time " + flight.getDepartTime() + " is not a valid 12-hour time.");
          }
            pw.println(flight.getDestination());
            pw.println(flight.getArriveDate());
          try {
            pw.println(arriveTimeTokenizer.nextToken());
            pw.println(arriveTimeTokenizer.nextToken());
          } catch (NoSuchElementException ex) {
            throw new InvalidTimeException("Arrival time " + flight.getArriveTime() + " is not a valid 12-hour time.");
          }
        }
      }


      pw.flush();
    }
  }
}
