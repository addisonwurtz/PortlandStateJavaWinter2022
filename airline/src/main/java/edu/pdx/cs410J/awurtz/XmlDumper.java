package edu.pdx.cs410J.awurtz;

import edu.pdx.cs410J.AirlineDumper;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Code for Airline project's XML Dumper
 */
public class XmlDumper implements AirlineDumper<Airline> {

    private final Writer writer;

    public XmlDumper(Writer writer) {
        this.writer = writer;
    }

    @Override
    public void dump(Airline airline) throws IOException {
        AirlineXmlHelper helper = new AirlineXmlHelper();
        Document doc = null;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            DOMImplementation dom = builder.getDOMImplementation();
            DocumentType docType = dom.createDocumentType("airline", helper.PUBLIC_ID, helper.SYSTEM_ID);
            doc = dom.createDocument(null, "airline", docType);

        } catch (ParserConfigurationException | DOMException e) {
            e.printStackTrace();
        }

        try {
            Element root = doc.getDocumentElement();

            Element airlineName = doc.createElement("name");
            root.appendChild(airlineName);
            airlineName.appendChild(doc.createTextNode(airline.getName()));

            ArrayList<Flight> flightList = (ArrayList<Flight>) airline.getFlights();
            for (Flight flight: flightList) {
                Element flightElement = doc.createElement("flight");
                root.appendChild(flightElement);

                Element flightNumber = doc.createElement("number");
                flightElement.appendChild(flightNumber);
                flightNumber.appendChild(doc.createTextNode(String.valueOf(flight.getNumber())));

                Element source = doc.createElement("src");
                flightElement.appendChild(source);
                source.appendChild(doc.createTextNode(flight.getSource()));

                Element departure = doc.createElement("depart");
                flightElement.appendChild(departure);
                departure.setAttribute("date", flight.getDepartDate());
                departure.setAttribute("time", flight.getDepartTime());

                Element destination = doc.createElement("dest");
                flightElement.appendChild(destination);
                destination.appendChild(doc.createTextNode(flight.getDestination()));

                Element arrival = doc.createElement("arrive");
                flightElement.appendChild(arrival);
                arrival.setAttribute("date", flight.getArriveDate());
                arrival.setAttribute("time", flight.getArriveTime());
            }
        } catch (DOMException e) {
            e.printStackTrace();
        }


    }
}
