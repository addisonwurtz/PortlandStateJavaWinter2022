package edu.pdx.cs410j.airline_android_app;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.AbstractFlight;

/**
 * Code for Airline class.
 */
public class Airline extends AbstractAirline {
    /** Name of airline */
    private final String name;
    /** List of airline's flights */
    private final ArrayList<Flight> flights = new ArrayList<>();

    /**
     * Airline Constructor
     * @param name of the airline
     */
    public Airline(String name) {
        this.name = name;
    }

    /*
    /**
     * Airline Constructor for XML files
     * @param root of DOM
     */
    /*
    public Airline(Element root) {
        NodeList nodeList = root.getChildNodes();
        Node node;
        Element element;

        for (int i = 0; i < nodeList.getLength(); i++) {
            node = nodeList.item(i);
            if(!(node instanceof Element)) {
                continue;
            }
            element = (Element) node;
            switch (element.getNodeName()) {
                case "name" -> this.name = element.getTextContent();
                case "flight" -> this.addFlight(new Flight(element));
                break;
                default:
                    throw new IllegalStateException("Unexpected value: " + element.getNodeName());
            }
        }

        //must also add flights from DOM
    }

     */

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void addFlight(AbstractFlight flight) {
        flights.add((Flight) flight);
        Collections.sort(flights);
    }

    //TODO I think this might be a problem later...
    public void addFlight(Flight flight) {
        flights.add(flight);
        Collections.sort(flights);
    }

    @Override
    public Collection<Flight> getFlights() {
        Collections.sort(flights);
        return flights;
    }
}
