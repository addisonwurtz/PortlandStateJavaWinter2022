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

      if(airline.getFlightCount() == 0) {
        pw.println("!!!");
      } else {
        airline.getFlights().forEach(flight -> {
          pw.println(flight.getNumber());
          pw.println(flight.getSource());
          pw.println(flight.getDepartureString());
          pw.println(flight.getDestination());
          pw.println(flight.getArrivalString());
        });
      }
      pw.flush();
    }
  }
}
