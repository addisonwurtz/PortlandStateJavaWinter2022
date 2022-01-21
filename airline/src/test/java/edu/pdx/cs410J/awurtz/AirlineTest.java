package edu.pdx.cs410J.awurtz;

import org.junit.jupiter.api.Test;


import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class AirlineTest {
    @Test
    void getNameReturnsName() {
        Airline airline = new Airline("Delta");
        assertThat(airline.getName(), equalTo("Delta"));
    }

    @Test
    void addFlightAddsFlight() {
        Airline airline = new Airline("Delta");
        Flight flight = new Flight(48, "PDX", "02/25/2022", "11:11", "LAX", "02/25/2022", "14:25");
        airline.addFlight(flight);
        assertThat(airline.getFlights(), hasItem(flight));
    }

    @Test
    void getFlightsReturnsFlightArrayList() {
        Airline airline = new Airline("Delta");
        Flight flight = new Flight(48, "PDX", "02/25/2022", "11:11", "LAX", "02/25/2022", "14:25");
        ArrayList<Flight> flightList = new ArrayList<>();
        flightList.add(flight);
        airline.addFlight(flight);
        assertThat(airline.getFlights(), equalTo(flightList));
    }
}
