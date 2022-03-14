package edu.pdx.cs410J.awurtz;

import edu.pdx.cs410J.AirlineDumper;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


/**
 * Code for Airline project's XML Dumper
 */
public class XmlDumper implements AirlineDumper<Airline> {

    private final Writer writer;

    public XmlDumper(Writer writer) {
        this.writer = writer;
    }

    /**
     * Dumps airline to specified xml file
     * @param airline to be written to xml
     * @throws IOException
     */
    @Override
    public void dump(Airline airline) throws IOException {
        AirlineXmlHelper helper = new AirlineXmlHelper();
        Document doc = null;

        if(airline == null) {
            throw new NullPointerException("The airline object was null, and could not be written to XML");
        }

        try {
            doc = buildDocument(helper);

        } catch (ParserConfigurationException | DOMException e) {
            System.err.println("There was an error while attempting to write the airline to xml: " + e.getMessage());
        }

        try {
            assert doc != null;
            Element root = doc.getDocumentElement();

            addElementNodeWithTextContent(doc, "name", root, airline.getName());

            ArrayList<Flight> flightList = (ArrayList<Flight>) airline.getFlights();
            for (Flight flight: flightList) {

                Element flightElement = doc.createElement("flight");
                root.appendChild(flightElement);

                addElementNodeWithTextContent(doc, "number", flightElement, String.valueOf(flight.getNumber()));
                addElementNodeWithTextContent(doc, "src", flightElement, flight.getSource());
                addDepartOrArriveElement(doc, "depart", flight.getDeparture(), flightElement);
                addElementNodeWithTextContent(doc, "dest", flightElement, flight.getDestination());
                addDepartOrArriveElement(doc, "arrive", flight.getArrival(), flightElement);
            }
        } catch (DOMException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("Null pointer exception thrown from xml dumper. ");
        }

        try {
            writeDomTreeToXml(helper, doc);
        } catch (TransformerException e) {
            e.printStackTrace(System.err);
            System.err.println("There was an problem writing airline to the XML file.");
            System.exit(1);
        }

        writer.close();
    }

    /**
     * Builds document object for DOM tree
     * @param helper class contains PUBLIC_ID and SYSTEM_ID for airline dtd
     * @return doc object
     * @throws ParserConfigurationException
     */
    private Document buildDocument(AirlineXmlHelper helper) throws ParserConfigurationException {
        Document doc;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        DocumentBuilder builder = factory.newDocumentBuilder();

        DOMImplementation dom = builder.getDOMImplementation();
        DocumentType docType = dom.createDocumentType("airline", helper.PUBLIC_ID, helper.SYSTEM_ID);
        doc = dom.createDocument(null, "airline", docType);
        return doc;
    }

    /**
     * Writes airline DOM tree to previously specified xml file
     * @param helper class contains PUBLIC_ID and SYSTEM_ID for airline dtd
     * @param doc is the populated Document object
     * @throws TransformerException
     */
    private void writeDomTreeToXml(AirlineXmlHelper helper, Document doc) throws TransformerException {
        Source src = new DOMSource(doc);
        Result res = new StreamResult(writer);

        TransformerFactory xFactory = TransformerFactory.newInstance();
        Transformer transformer = xFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, helper.SYSTEM_ID);
        transformer.transform(src, res);
    }

    /**
     * Adds the departure or arrival elements and children to the DOM tree (including date and time elements and their
     * associated attributes)
     * @param doc is the Document object
     * @param elementName specified either "depart" or "arrive" element
     * @param flightDate is either depart or arrive member of flight
     * @param flightElement
     */
    private void addDepartOrArriveElement(Document doc, String elementName, Date flightDate, Element flightElement) {
        Element rootElement = doc.createElement(elementName);
        flightElement.appendChild(rootElement);

        //convert date to calendar
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(flightDate);

        Element date = doc.createElement("date");
        rootElement.appendChild(date);
        date.setAttribute("day", String.valueOf(calendar.get(Calendar.DATE)));
        date.setAttribute("month", String.valueOf(calendar.get(Calendar.MONTH)));
        date.setAttribute("year", String.valueOf(calendar.get(Calendar.YEAR)));

        Element time = doc.createElement("time");
        rootElement.appendChild(time);
        time.setAttribute("hour", String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
        time.setAttribute("minute", String.valueOf(calendar.get(Calendar.MINUTE)));
    }


    /**
     * Adds element with text content to dom tree
     * @param doc is the Document object
     * @param elementName is the name of the element node
     * @param flightElement is the parent the new element
     * @param textNodeContent is the text that will populate the new element
     */
    private void addElementNodeWithTextContent(Document doc, String elementName, Element flightElement, String textNodeContent) {
        Element flightNumber = doc.createElement(elementName);
        flightElement.appendChild(flightNumber);
        flightNumber.appendChild(doc.createTextNode(textNodeContent));
    }

}
