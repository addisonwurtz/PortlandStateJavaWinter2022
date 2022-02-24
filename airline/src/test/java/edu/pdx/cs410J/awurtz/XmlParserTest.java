package edu.pdx.cs410J.awurtz;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class XmlParserTest {
    @Test
    void canParseValidXmlFile() throws ParserException {
       XmlParser parser = new XmlParser("ValidXMLTestFile");

       Airline airline = parser.parse();

       assertThat(airline.getName(), containsString("Valid Airlines"));
       assertThat(airline.getFlights().toArray().length, equalTo(0));
    }

    @Test
    void cantParseInvalidXmlFile() throws ParserException {
        XmlParser parser = new XmlParser("InvalidXMLTestFile");

        assertThrows(ParserException.class, () -> parser.parse());
    }
}
