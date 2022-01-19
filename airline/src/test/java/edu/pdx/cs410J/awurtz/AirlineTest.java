package edu.pdx.cs410J.awurtz;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AirlineTest {
    @Test
    void getNameReturnsName() {
        Airline airline = new Airline("Delta");
        assertThat(airline.getName(), equalTo("Delta"));
    }

}
