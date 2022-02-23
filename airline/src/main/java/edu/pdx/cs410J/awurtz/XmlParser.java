package edu.pdx.cs410J.awurtz;

import edu.pdx.cs410J.AirlineParser;
import edu.pdx.cs410J.ParserException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.Reader;

/**
 * Code for Airline project's XML file parser class.
 */
public class XmlParser implements AirlineParser<Airline> {
    private final File file;

    /**
     * XML parser constructor
     * @param fileName string representation of XML file name
     */
    public XmlParser(String fileName) {
        file = new File(fileName);
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

        doc = builder.parse(file);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new ParserException("XML file (" + this.file + ") could not be read.");
        }

        Element root = (Element) doc.getDocumentElement();
        return new Airline(root);
    }
}
