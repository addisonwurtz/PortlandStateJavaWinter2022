package edu.pdx.cs410J.awurtz;

import edu.pdx.cs410J.ParserException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static edu.pdx.cs410J.awurtz.PrettyPrinter.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrettyPrinterTest {

    @Test
    void flightDurationInMinutesReturnsCorrectDuration() {
        Flight flight = new Flight(66, "PDX", "2/2/2022", "10:28 am",
                "SFO", "2/2/2022", "11:30 am");
        assertThat(PrettyPrinter.flightDurationInMinutes(flight), equalTo(62));
    }

    @Test
    void flightInformationIsPrettyPrinted() throws IOException {
        String airlineName = "Delta";
        Airline airline = new Airline(airlineName);
        Flight flight = new Flight(12, "PDX", "01/24/2022", "11:11 am", "SFO",
                "01/24/2022", "12:12 pm");

        airline.addFlight(flight);

        StringWriter sw = new StringWriter();
        PrettyPrinter dumper = new PrettyPrinter(sw);
        dumper.dump(airline);
        String text = sw.toString();

        assertThat(text, containsString("12"));
        assertThat(text, containsString("Portland, OR"));
        assertThat(text, containsString("Mon, Jan 24 2022 at 11:11 AM"));
        assertThat(text, containsString("San Francisco, CA"));
        assertThat(text, containsString("Mon, Jan 24 2022 at 12:12 PM"));
        assertThat(text, containsString("61 minutes"));
    }

    @Test
    void prettyPrinterPrintsToTextFile() throws IOException {
        Airline airline = new Airline("Alaska");
        airline.addFlight(new Flight(456, "DEN", "2/1/2022", "6:53 pm", "LAX",
                "2/1/2022", "11:27 pm"));
        airline.addFlight(new Flight(777, "ABQ", "3/06/2022", "11:00 am",
                "AUS", "3/6/2022", "1:13 pm"));
        airline.addFlight(new Flight(336, "ABQ", "2/09/2022", "3:00 pm",
                "AUS", "2/9/2022", "5:35 pm"));

        File textFile = new File("PrettyPrinterTest");
        PrettyPrinter prettyPrinter = new PrettyPrinter(new FileWriter(textFile));
        prettyPrinter.dump(airline);

        BufferedReader reader = new BufferedReader(new FileReader(textFile));
        assertThat(reader.readLine(), containsString("Alaska"));
        reader.close();
    }


}
