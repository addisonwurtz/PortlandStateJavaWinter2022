package edu.pdx.cs410J.awurtz;

import edu.pdx.cs410J.AirlineParser;
import edu.pdx.cs410J.ParserException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

/**
 * Code for Airline project's XML file parser class.
 */
public class XmlParser implements AirlineParser<Airline> {
    private final String airlineString;

    /**
     * XML parser constructor
     * @param airlineString string representation airline as XML object
     */
    public XmlParser(String airlineString) {
        this.airlineString = airlineString;
    }


    /**
     * Parses Airline from XML file
     * @return Airline from XML file
     * @throws ParserException
     */
    @Override
    public Airline parse() throws ParserException {
        Document doc = null;
        AirlineXmlHelper helper = new AirlineXmlHelper();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);

            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(helper);
            builder.setEntityResolver(helper);

            InputSource xmlInput = new InputSource(new StringReader(airlineString));
            doc = builder.parse(xmlInput);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new ParserException("XML file (" + this.airlineString + ") could not be read.");
        }

        Element root = (Element) doc.getDocumentElement();
        return new Airline(root);
    }
}
