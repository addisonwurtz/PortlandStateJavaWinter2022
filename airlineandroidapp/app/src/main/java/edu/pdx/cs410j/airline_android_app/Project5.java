package edu.pdx.cs410j.airline_android_app;

import java.io.*;
import java.util.Map;

/**
 * The main class that parses the command line and communicates with the
 * Airline server using REST.
 */
public class Project5 {

    public static final String MISSING_ARGS = "Missing command line arguments";
/*
    public static void main(String... args) throws IOException {
        Boolean searchOption = false;
        Boolean printOption = false;

        String hostName = null;
        String portString = null;
        String airlineName;
        String source;
        String destination;

        int i = 0;
        //parse options
        while(i < args.length && args[i].startsWith("-")) {
            switch (args[i]) {
                case "-README" -> {
                    printReadmeAndExit();
                }
                case "-host" -> {
                    hostName = args[++i];
                }
                case "-port" -> {
                    portString = args[++i];
                }
                case "-search" -> {
                    searchOption = true;
                }
                case "-print" -> {
                    printOption = true;
                }
            }
            i++;
        }

        //errors if either host or port is omitted
        if(args.length == 0) {
            usage(MISSING_ARGS);
            return;
        } else if (hostName == null) {
            usage( "Missing host" );
            return;

        } else if ( portString == null) {
            usage( "Missing port" );
            return;
        }

        int port;
        try {
            port = Integer.parseInt( portString );
        } catch (NumberFormatException ex) {
            usage("Port \"" + portString + "\" must be an integer");
            return;
        }

        AirlineRestClient client = new AirlineRestClient(hostName, port);

        try {
            //Pretty print all flights from specified airline
            if (args.length == 5) {
                airlineName = args[i];
                Airline airline = client.getAirline(airlineName);
                new PrettyPrinter(new OutputStreamWriter(System.out)).dump(airline);
            } else if(searchOption) {
                if(args.length - i < 3) {
                    System.err.println("-search options requires the airlineName, source airport, and destination airport.");
                    usage("Missing required arguments for -search");
                    return;
                }
                airlineName = args[i];
                source = args[++i];
                destination = args[++i];
                Airline airline = client.searchFlights(airlineName, source, destination);
                if(airline == null) {
                    System.out.println("There were no " + airlineName + " flights from " + source + " to " + destination);
                    System.exit(1);
                }
                if(airline.getFlights().size() == 0) {
                    System.out.println("There were no " + airlineName + " flights from " + source + " to " + destination);
                    System.exit(1);
                }
                else {
                    new PrettyPrinter(new OutputStreamWriter(System.out)).dump(airline);
                }
            } else {
                //add the flight
                Flight flight = new Flight(args);
                airlineName = args[i];
                client.addFlight(airlineName, flight);
                if(printOption) {
                    System.out.println(flight.toString());
                }
            }

        } catch (IOException | ParserException ex ) {
            error("While contacting server: " + ex);
            return;
        } catch (HttpRequestHelper.RestException ex) {
            error(ex.getMessage());
        }

        System.exit(0);
    }

 */

    private static void printReadmeAndExit() throws IOException {
        InputStream readme = Project5.class.getResourceAsStream("readme.txt");
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(readme))
        ) {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                System.out.println(line);
            }
        }

    }

    private static void error( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);

        System.exit(1);
    }

    /**
     * Prints usage information for this program and exits
     * @param message An error message to print
     */
    private static void usage( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);
        err.println();
        err.println("usage: java Project5 host port [host_name] [port_number]");
        err.println("  host         Host of web server");
        err.println("  port         Port of web server");
        err.println("  word         Word in dictionary");
        err.println("  definition   Definition of word");
        err.println();
        err.println("This simple program posts words and their definitions");
        err.println("to the server.");
        err.println("If no definition is specified, then the word's definition");
        err.println("is printed.");
        err.println("If no word is specified, all dictionary entries are printed");
        err.println();

        System.exit(1);
    }


}

class MissingCommandLineArgumentException extends RuntimeException {
    private final String missingArgument;

    public MissingCommandLineArgumentException(String missingArgument) {
        this.missingArgument = missingArgument;
    }

    public String getMissingArgument() {return missingArgument;}
}

class InvalidFlightNumberException extends NumberFormatException {
    private final String invalidFlightNumber;

    public InvalidFlightNumberException(String invalidFlightNumber) {
        this.invalidFlightNumber = invalidFlightNumber;}

    public String getInvalidFlightNumber() {return invalidFlightNumber;}
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


