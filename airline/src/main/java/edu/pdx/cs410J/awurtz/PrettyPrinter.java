package edu.pdx.cs410J.awurtz;

import edu.pdx.cs410J.AirlineDumper;

import java.io.IOException;
import java.io.Writer;

/**
 * Code for the <code>Pretty Printer</code> class
 */

public class PrettyPrinter implements AirlineDumper<Airline> {
    private final Writer writer;

    public PrettyPrinter(Writer writer) {
        this.writer = writer;
    }

    @Override
    public void dump(Airline airline) throws IOException {

    }
}
