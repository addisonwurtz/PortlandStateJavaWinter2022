package edu.pdx.cs410J.awurtz;

import edu.pdx.cs410J.ParserException;

import java.io.*;
import java.util.Map;

/**
 * The main class that parses the command line and communicates with the
 * Airline server using REST.
 */
public class Project5 {

    public static final String MISSING_ARGS = "Missing command line arguments";

    public static void main(String... args) throws IOException {
        Boolean hostNameOption = false;
        Boolean portStringOption = false;
        Boolean searchOption = false;
        Boolean printOption = false;

        String hostName = null;
        String portString = null;
        String searchString = null;
        String airlineName = null;
        String flightNumberString = null;
        String source = null;
        String departure = null;
        String destination = null;
        String arrival = null;

        int i = 0;
        //parse options
        while(i < args.length && args[i].contains("-")) {
            switch (args[i]) {
                case "-README" -> {
                    printReadmeAndExit();
                }
                case "-host" -> {
                    hostNameOption = true;
                    hostName = args[++i];
                }
                case "-port" -> {
                    portStringOption = true;
                    portString = args[++i];
                }
                case "-search" -> {
                    searchOption = true;
                    searchString = args[++i];
                }
                case "-print" -> {
                    printOption = true;
                }
            }
            i++;
        }


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
//        else if (airlineName == null) {
//            usage("Missing airline name");
//        }

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
                //TODO implement search
                usage("Search is not yet implemented.");
                return;
            } else {
                //add the flight
                airlineName = args[i];
                flightNumberString = args[++i];
                client.addFlight(airlineName, new Flight(Integer.parseInt(flightNumberString)));
                //client.addFlight(airlineName, new Flight(args));
            }

        } catch (IOException | ParserException ex ) {
            error("While contacting server: " + ex);
            return;
        }

        System.exit(0);
    }

    private static void printReadmeAndExit() throws IOException {
        InputStream readme = Project5.class.getResourceAsStream("README.txt");
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

/**
 *  Exception class is used when the name of the airline from the commandline does not match the name of
 *  the airline in the file referenced with the -textFile commandline option.
 */
class AirlineFromFileDoesNotMatchAirlineFromCommandLineException extends RuntimeException {
    private final Airline airlineFromFile;
    private final Airline airlineFromCommandLine;

    public AirlineFromFileDoesNotMatchAirlineFromCommandLineException(Airline airlineFromFile,
                                                                      Airline airlineFromCommandLine) {
        this.airlineFromFile = airlineFromFile;
        this.airlineFromCommandLine = airlineFromCommandLine;
    }

    public String getAirlineFromFile() {
        return airlineFromFile.getName();
    }

    public String getAirlineFromCommandLine() {
        return airlineFromCommandLine.getName();
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


