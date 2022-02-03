package edu.pdx.cs410J.awurtz;

import org.junit.jupiter.api.Test;


import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AirlineTest {
    @Test
    void getNameReturnsName() {
        Airline airline = new Airline("Delta");
        assertThat(airline.getName(), equalTo("Delta"));
    }

    @Test
    void addFlightAddsFlight() {
        Airline airline = new Airline("Delta");
        Flight flight = new Flight(48, "PDX", "02/25/2022", "11:11 am", "LAX",
                "02/25/2022", "2:25 pm");
        airline.addFlight(flight);
        assertThat(airline.getFlights(), hasItem(flight));
    }

    @Test
    void getFlightsReturnsFlightArrayList() {
        Airline airline = new Airline("Delta");
        Flight flight = new Flight(48, "PDX", "02/25/2022", "11:11 am", "LAX",
                "02/25/2022", "2:25 pm");
        ArrayList<Flight> flightList = new ArrayList<>();
        flightList.add(flight);
        airline.addFlight(flight);
        assertThat(airline.getFlights(), equalTo(flightList));
    }

    @Test
    void newFlightsIsAddedInSortedOrderToAirline() {
        Airline airline = new Airline("Delta");
        Flight portlandFlight = new Flight(48, "PDX", "02/25/2022", "11:11 am", "LAX",
                "02/25/2022", "2:25 pm");
        Flight denverFlight = new Flight(456, "DEN", "2/1/2022", "6:53 pm", "LAX", "2/1/2022", "11:27 pm");


        airline.addFlight(portlandFlight);
        airline.addFlight(denverFlight);

        ArrayList<Flight> flightList = (ArrayList<Flight>) airline.getFlights();

        assertEquals(flightList.get(0), denverFlight);
        assertEquals(flightList.get(1), portlandFlight);
    }
}
