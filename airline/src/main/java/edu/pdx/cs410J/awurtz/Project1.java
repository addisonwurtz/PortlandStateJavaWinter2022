package edu.pdx.cs410J.awurtz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

/**
 * The main class for the CS410J airline Project
 */
public class Project1 {

  public static void parseCommandLineArgumentsAndReturnFlight(String[] args) {
    if (args[0].equals("-README") || args[1].equals("-README")) {

      try {
        InputStream readme = Project1.class.getResourceAsStream("README.txt");
        assert readme != null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(readme));
        System.out.println(reader.readLine());
      } catch (IOException ex) {
        printErrorMessageAndExit("README file was not be found.");
      }
    } else {
      if (args[0].equals("-print")) {
        createFlightAndPrintDescription(args);
      }
    }
  }

  public static void createFlightAndPrintDescription(String[] args) {
    String airline = args[1];
    try {
      Integer flightNumber = parseInt(args[2]);
      String source = args[3];
      if (source.length() > 3 || !isAlpha(source)) {
        throw new InvalidSourceException(source);
      }
      String departDate = args[4];
      if (!parseDate(departDate)) {
         throw new InvalidDateException(departDate);
      }
      String departTime = args[5];
      String destination = args[6];
      String arriveTime = args[7];
      String arriveDate = args[8];

    } catch (NumberFormatException ex) {
      printErrorMessageAndExit("Flight number is not an integer.");
    } catch (InvalidSourceException ex) {
      printErrorMessageAndExit(ex.invalidSource + " is not a 3 letter airport code.");
    }
    catch (InvalidDateException ex) {
      printErrorMessageAndExit(ex.invalidDate + " is not a valid date.");
    }

  }

  static boolean parseDate(String dateString) {
    return dateString.matches("^(((0[13578]|(10|12))/(0[1-9]|[1-2][0-9]|3[0-1]))|(02/(0[1-9]|[1-2][0-9]))|((0[469]|11)/(0[1-9]|[1-2][0-9]|30)))/[0-9]{4}$");

  }


  private static boolean isAlpha(String string) {
    if (string == null) {
      return false;
    }

    for (int i = 0; i < string.length(); i++) {
      char c = string.charAt(i);
      if (!(c >= 'A' && c <= 'Z') && !(c >= 'a' && c <= 'z')) {
        return false;
      }
    }
    return true;
  }


  public static void main(String[] args) {
    Flight flight = new Flight();

    if (args.length < 9) {
      printErrorMessageAndExit("Missing command line arguments." + printCommandLineInterfaceDescription());
    } else {
      parseCommandLineArgumentsAndReturnFlight(args);
    }


    System.exit(1);
  }


  private static void printErrorMessageAndExit(String message) {
    System.err.println(message);
    System.exit(1);
  }

  private static String printCommandLineInterfaceDescription() {
    return """
               args are (in this order): [options] <args>
                 airline                   The name of the airline
                 flightNumber              The flight number
                 src                       Three-letter code of departure airport
                 departDate                Departure date
                 departTime                Departure time (24-hour time)
                 dest                      Three-letter code of arrival airport
                 arriveDate                Arrival date
                 arriveTime                Arrival time (24-hour time)
               options are (options may appear in any order):
                 -print                    Prints a description of the new flight
                 -README                   Prints a README for this project and exits
               Date and time should be in the format: mm/dd/yyy hh:mm
            """;
  }
}


  class InvalidSourceException extends RuntimeException {
    public String invalidSource;

    public InvalidSourceException(String invalidSource) {
      this.invalidSource = invalidSource;
    }
  }

  class InvalidDateException extends RuntimeException {
    public String invalidDate;

    public InvalidDateException(String invalidDate) {
      this.invalidDate = invalidDate;
    }
  }


