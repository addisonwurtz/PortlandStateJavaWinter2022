package edu.pdx.cs410j.airline_android_app;

import java.io.*;

/**
 * The main class that parses the command line and communicates with the
 * Airline server using REST.
 */
public class Project5 {

    public static final String MISSING_ARGS = "Missing command line arguments";


}

class MissingFlightArgumentException extends RuntimeException {
    private final String missingArgument;

    public MissingFlightArgumentException(String missingArgument) {
        this.missingArgument = missingArgument;
    }

    public String getMissingArgument() {return missingArgument;}
}

class InvalidFlightNumberException extends NumberFormatException {
    private final String message;

    public InvalidFlightNumberException(String message) {
        this.message = message;}

    public String getMessage() {return message;}
}

class InvalidAirportCodeException extends RuntimeException {
    private final String invalidAirportCode;

    public InvalidAirportCodeException(String invalidAirportCode) {
        this.invalidAirportCode = invalidAirportCode;
    }

    public String getInvalidAirportCode() {return invalidAirportCode;}
}

class InvalidDateException extends RuntimeException {
    private final String invalidDate;

    public InvalidDateException(String invalidDate) {
        this.invalidDate = invalidDate;
    }

    public String getInvalidDate() {return invalidDate;}
}

class InvalidTimeException extends RuntimeException {
    public InvalidTimeException(String invalidTimeMessage) {
        super(invalidTimeMessage);
    }
}

class InvalidDepartureException extends RuntimeException {
    public InvalidDepartureException(String message) {
        super(message);
    }
}

class InvalidArrivalException extends RuntimeException {
    public InvalidArrivalException(String message) {
        super(message);
    }
}


