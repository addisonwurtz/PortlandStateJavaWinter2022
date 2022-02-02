package edu.pdx.cs410J.awurtz;

import edu.pdx.cs410J.AbstractAirline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Code for Airline class.
 */
public class Airline extends AbstractAirline<Flight> {
  private final String name;
  private final ArrayList<Flight> flights = new ArrayList<>();

  /**
   * Airline Constructor
   * @param name of the airline
   */
  public Airline(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
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
