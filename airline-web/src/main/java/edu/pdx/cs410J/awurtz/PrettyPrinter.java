package edu.pdx.cs410J.awurtz;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.AirlineDumper;
import edu.pdx.cs410J.AirportNames;

import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PrettyPrinter implements AirlineDumper<Airline> {
  private final Writer writer;
  private static final String prettyDateTimePattern = "EEE, MMM dd yyyy 'at' hh:mm a";

  @VisibleForTesting
  static String formatWordCount(int count )
  {
    return String.format( "Dictionary on server contains %d words", count );
  }

  @VisibleForTesting
  static String formatDictionaryEntry(String word, String definition )
  {
    return String.format("  %s : %s", word, definition);
  }


  public PrettyPrinter(Writer writer) {
    this.writer = writer;
  }

  public void dump(Map<String, String> dictionary) {
    try (
      PrintWriter pw = new PrintWriter(this.writer)
    ) {

      pw.println(formatWordCount(dictionary.size()));

      for (Map.Entry<String, String> entry : dictionary.entrySet()) {
        String word = entry.getKey();
        String definition = entry.getValue();
        pw.println(formatDictionaryEntry(word, definition));
      }

      pw.flush();
    }
  }

  @Override
  public void dump(Airline airline) {
    SimpleDateFormat formatter = new SimpleDateFormat(prettyDateTimePattern);

    try (
            PrintWriter pw = new PrintWriter(this.writer)
    ) {
      pw.println(airline.getName() + " Airlines");

      ArrayList<Flight> flightList = (ArrayList<Flight>) airline.getFlights();
      Collections.sort(flightList);
      if (!flightList.isEmpty()) {
        for (Flight flight : flightList) {
          pw.println();
          pw.println("Flight " + flight.getNumber() + "\t\t" + flight.getSource() + " -> " + flight.getDestination() + "\t\t" +
                  flightDurationInMinutes(flight) + " minutes");
          pw.println("Departs from " + AirportNames.getName(flight.getSource()) + " on " +
                  formatter.format(flight.getDeparture()));
          pw.println("Arrives at " + AirportNames.getName(flight.getDestination()) + " on " + formatter.format((flight.getArrival())));
        }
      }

    }
  }

  public static int flightDurationInMinutes(Flight flight) {

    long durationInMillis = Math.abs(flight.getArrival().getTime() - flight.getDeparture().getTime());
    return Math.toIntExact(TimeUnit.MINUTES.convert(durationInMillis, TimeUnit.MILLISECONDS));
  }
}
