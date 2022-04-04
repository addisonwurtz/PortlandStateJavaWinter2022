package edu.pdx.cs410j.airline_android_app;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Map;

public class TextDumper {
  private final Writer writer;

  public TextDumper(Writer writer) {
    this.writer = writer;
  }

  public void dump(Airline airline) {
    try (
      PrintWriter pw = new PrintWriter(this.writer)
    ){
      pw.println(airline.getName());

      int count = airline.getFlightCount();
      if(count == 0) {
        pw.println("!!!");
      } else {
        for (Flight flight : airline.getFlights()) {
          pw.println(flight.getNumber());
          pw.println(flight.getSource());
          pw.println(flight.getDepartureString());
          pw.println(flight.getDestination());
          pw.println(flight.getArrivalString());

          --count;
          if (count > 0) {
            pw.println("***");
          }
        }
      }
      pw.flush();
    }
  }
}
