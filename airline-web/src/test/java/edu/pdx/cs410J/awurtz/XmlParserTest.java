package edu.pdx.cs410J.awurtz;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class XmlParserTest {

    //These tests are disabled because they rely on files that are not bundled in the submit.sh script
    @Disabled
    @Test
    void canParseValidXmlFile() throws ParserException {
        XmlParser parser = new XmlParser("ValidXMLTestFile");

        Airline airline = parser.parse();

        assertThat(airline.getName(), containsString("Valid Airlines"));
        assertThat(airline.getFlights().toArray().length, equalTo(1));
    }

    @Disabled
    @Test
    void cantParseInvalidXmlFile() {
        XmlParser parser = new XmlParser("InvalidXMLTestFile");

        assertThrows(ParserException.class, parser::parse);
    }



}

