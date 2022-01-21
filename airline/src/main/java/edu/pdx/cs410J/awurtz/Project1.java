package edu.pdx.cs410J.awurtz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import static java.lang.Integer.*;

/**
 * The main class for the CS410J airline Project
 */
public class Project1 {

  public static void parseCommandLineArgumentsAndReturnFlight(String[] args) throws IOException {
    if (args[0].equals("-README") || args[1].equals("-README")) {
        InputStream readme = Project1.class.getResourceAsStream("README.txt");
        assert readme != null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(readme));
        System.out.println(reader.readLine());

    } else {
      if (args.length < 9) {
        printErrorMessageAndExit("Missing command line arguments." + printCommandLineInterfaceDescription());
      }
      if (args.length > 9) {
        printErrorMessageAndExit("Too many command line arguments." + printCommandLineInterfaceDescription());
      }
      if (args[0].equals("-print")) {
        System.out.println(parseArgsAndCreateFlight(args).toString());
      }
    }
  }

  public static Flight parseArgsAndCreateFlight(String[] args) {
    String airline = args[1];
    int flightNumber;
    try {
      flightNumber = parseInt(args[2]);
    } catch(NumberFormatException ex) {
      throw new InvalidFlightNumberException(args[2]);
    }
    String source = args[3].toUpperCase();
    if (source.length() > 3 || !isAlpha(source)) {
      throw new InvalidSourceException(source);
    }
    String departDate = parseDate(args[4]);

    String departTime = parseTime(args[5]);

    String destination = args[6].toUpperCase();

    if (destination.length() > 3 || !isAlpha(destination)) {
      throw new InvalidSourceException(destination);
    }

    String arriveDate = parseDate(args[7]);

    String arriveTime = parseTime(args[8]);

    return new Flight(flightNumber, source, departDate,departTime,destination,arriveTime, arriveDate);

  }

  static String parseDate(String dateString) throws InvalidDateException{
    StringTokenizer stringTokenizer = new StringTokenizer(dateString, "/");

    if(stringTokenizer.countTokens() != 3) {
      throw new InvalidDateException(dateString);
    }

    int month, day, year;
    try {
      month = Integer.parseInt(stringTokenizer.nextToken());
      day = Integer.parseInt(stringTokenizer.nextToken());
      year = Integer.parseInt(stringTokenizer.nextToken());
      } catch (NumberFormatException ex) {
      throw new InvalidDateException(dateString);
    }
    if(month < 1 || month > 12 || day < 1 || day > 31 || year < 1000 || year > 9999) {
      throw new InvalidDateException(dateString);
    }
    if((month == 2 && day > 29) || ((month == 4 || month == 6 || month == 9 || month == 11) && (day > 30))) {
      throw new InvalidDateException(dateString);
    }
    return dateString;
  }

  static String parseTime(String timeString) {
    StringTokenizer stringTokenizer = new StringTokenizer(timeString, ":");
    
    if(stringTokenizer.countTokens() != 2) {
      throw new InvalidTimeException(timeString);
    }

    try{
      int hour = Integer.parseInt(stringTokenizer.nextToken());
      int minute = Integer.parseInt(stringTokenizer.nextToken());
    } catch (NumberFormatException ex) {
      throw new InvalidTimeException(timeString);
    }

    return timeString;
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

    if (args.length == 0) {
      printErrorMessageAndExit("Missing command line arguments." + printCommandLineInterfaceDescription());
    }
    try {
      parseCommandLineArgumentsAndReturnFlight(args);
    } catch (IOException ex) {
      printErrorMessageAndExit("README file was not be found.");
    } catch (InvalidFlightNumberException ex) {
      printErrorMessageAndExit("Flight number " + ex.getInvalidFlightNumber() + " is not an integer." + printCommandLineInterfaceDescription());
    } catch (InvalidSourceException ex) {
      printErrorMessageAndExit(ex.getInvalidSource() + " is not a 3 letter airport code." + printCommandLineInterfaceDescription());
    } catch (InvalidDateException ex) {
      printErrorMessageAndExit(ex.getInvalidDate() + " is not a valid date." + printCommandLineInterfaceDescription());
    } catch (InvalidTimeException ex) {
      printErrorMessageAndExit(ex.getInvalidTime() + " is not a valid time" + printCommandLineInterfaceDescription());
    }


    System.exit(1);
  }


  private static void printErrorMessageAndExit(String message) {
    System.err.println(message);
    System.exit(1);
  }

  private static String printCommandLineInterfaceDescription() {
    return "\n\nargs are (in this order): [options] <args>\n" +
            "\tairline                   The name of the airline\n" +
            "\tflightNumber              The flight number\n" +
            "\tsrc                       Three-letter code of departure airport\n" +
            "\tdepartDate                Departure date\n" +
            "\tdepartTime                Departure time (24-hour time)\n" +
            "\tdest                      Three-letter code of arrival airport\n" +
            "\tarriveDate                Arrival date\n" +
            "\tarriveTime                Arrival time (24-hour time)\n" +
            "options are (options may appear in any order):\n" +
            "\t-print                    Prints a description of the new flight\n" +
            "\t-README                   Prints a README for this project and exits\n" +
            "Date and time should be in the format: mm/dd/yyy hh:mm\n";

  }
}


class InvalidFlightNumberException extends NumberFormatException {
  private final String invalidFlightNumber;

  public InvalidFlightNumberException(String invalidFlightNumber) { this.invalidFlightNumber = invalidFlightNumber;}
  
  public String getInvalidFlightNumber() {
    return invalidFlightNumber;
  }
}

class InvalidSourceException extends RuntimeException {
    private final String invalidSource;

    public InvalidSourceException(String invalidSource) { this.invalidSource = invalidSource;
    }

    public String getInvalidSource() {
      return invalidSource;
    }
  }

class InvalidDateException extends RuntimeException {
   private final String invalidDate;

   public InvalidDateException(String invalidDate) {
      this.invalidDate = invalidDate;
    }

   public String getInvalidDate() {
     return invalidDate;
   }
}

class InvalidTimeException extends RuntimeException {
  private final String invalidTime;

  public InvalidTimeException(String invalidTime) {this.invalidTime = invalidTime;}

  public String getInvalidTime() {
    return invalidTime;
  }
}

