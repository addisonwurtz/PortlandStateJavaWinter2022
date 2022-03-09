package edu.pdx.cs410J.awurtz;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;

public class XmlDumperTest {

    @Test
    public void XmlDumperWritesAirlineToFile() throws IOException {
        String fileName = "XmlDumperTestFile";
        XmlDumper dumper = new XmlDumper(new FileWriter(fileName));

        Airline airline = new Airline("Delta");
        Flight portlandFlight = new Flight(48, "PDX", "02/25/2022", "11:11 am",
                "LAX",
                "02/25/2022", "2:25 pm");
        Flight denverFlight = new Flight(456, "DEN", "2/1/2022", "6:53 pm",
                "LAX", "2/1/2022", "11:27 pm");

        airline.addFlight(portlandFlight);
        airline.addFlight(denverFlight);

        dumper.dump(airline);
        assertTrue(Files.exists(Path.of(fileName)));
        String fileLines = Files.readString(Path.of(fileName));
        assertThat(fileLines, containsString("Delta"));
        assertThat(fileLines, containsString("<number>48</number>"));
        assertThat(fileLines, containsString("<number>456</number>"));
        Files.deleteIfExists(Path.of(fileName));
    }

    @Test
    public void airlineIsDumpedInXMLFormat() throws IOException {
        String airlineName = "Test Airline";
        Airline airline = new Airline(airlineName);

        StringWriter sw = new StringWriter();
        XmlDumper dumper = new XmlDumper(sw);
        dumper.dump(airline);

        String text = sw.toString();
        assertThat(text, containsString("<name>" + airlineName + "</name>"));
    }

    @Test
    void canParseValidXmlAirline() throws IOException {
        Airline airline = new Airline("Test Airline");
        airline.addFlight(new Flight(777, "PDX"));

        StringWriter sw = new StringWriter();
        XmlDumper dumper = new XmlDumper(sw);
        dumper.dump(airline);

        String airlineXml = sw.toString();
        assertThat(airlineXml, containsString("<name>Test Airline</name>"));
        assertThat(airlineXml, containsString("<number>777</number>"));
    }
}
