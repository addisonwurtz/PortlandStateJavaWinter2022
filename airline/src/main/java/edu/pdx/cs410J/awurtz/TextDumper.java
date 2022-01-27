package edu.pdx.cs410J.awurtz;

import edu.pdx.cs410J.AirlineDumper;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;


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
          if(!firstIteration) {
            pw.println("***");
          }
          else {
            firstIteration = false;
          }
          pw.println(flight.getNumber());
          pw.println(flight.getSource());
          pw.println(flight.getDepartDate());
          pw.println(flight.getDepartTime());
          pw.println(flight.getDestination());
          pw.println(flight.getArriveDate());
          pw.println(flight.getArriveTime());
        }
      }


      pw.flush();
    }
  }
}
