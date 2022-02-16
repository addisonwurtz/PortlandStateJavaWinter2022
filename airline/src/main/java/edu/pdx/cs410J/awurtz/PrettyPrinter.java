package edu.pdx.cs410J.awurtz;

import edu.pdx.cs410J.AirlineDumper;
import edu.pdx.cs410J.AirportNames;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Code for the <code>Pretty Printer</code> class
 */

public class PrettyPrinter implements AirlineDumper<Airline> {
    private final Writer writer;
    static final String prettyDateTimePattern = "EEE, MMM dd yyyy 'at' hh:mm a";

    public PrettyPrinter(Writer writer) {
        this.writer = writer;
    }

    @Override
    public void dump(Airline airline) {
        SimpleDateFormat formatter = new SimpleDateFormat(prettyDateTimePattern);

        try (
                PrintWriter pw = new PrintWriter(this.writer)
        ) {
            pw.println(airline.getName() + " Airlines");

            ArrayList<Flight> flightList = (ArrayList<Flight>) airline.getFlights();
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
