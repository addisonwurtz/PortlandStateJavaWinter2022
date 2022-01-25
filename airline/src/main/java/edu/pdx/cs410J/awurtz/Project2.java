package edu.pdx.cs410J.awurtz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

import static java.lang.Integer.*;

/**
 * The main class for the CS410J Airline Project
 */
public class Project2 {

  /**
   * The checkOptions method initially checks for -README or -print options in args, checks for the correct number of args, and then
   * directs the flow of the program appropriates.
   *
   * @param args
   *        The String of commandline arguments that will be parsed and, if they are correct, used to build a flight
   * @throws IOException is thrown if there is a problem accessing the readme file. The exception is caught in the main method.
   */
  public static void checkOptions(String[] args) throws IOException {
    if (args[0].equals("-README") || args[1].equals("-README")) {
        System.out.println(getReadMe());
    }
    if(args[0].equals("-print")) {
      if (args.length > 9) {
        printErrorMessageAndExit("Too many command line arguments.");
      }
      if (args.length < 9) {
        printErrorMessageAndExit("Missing command line arguments.");
      }
      parseArgsAndCreateFlight(Arrays.copyOfRange(args, 2, args.length));
    }
    else {
      if(args.length > 8) {
        printErrorMessageAndExit("Too many command line arguments.");
      }
      if(args.length < 8) {
        printErrorMessageAndExit("Missing command line arguments.");
      }
      parseArgsAndCreateFlight(Arrays.copyOfRange(args, 1, args.length));
    }

  }

  /**
   * The parseArgsAndCreateFlight method is called in order to individually check that each of the args is properly formatted and, if they are,
   * returns a flight built from the freshly parsed parameters.
   * @param args
   *        The commandline arguments (options are ignored in this method)
   * @return
   *        Returns the freshly constructed flight.
   */
 static Flight parseArgsAndCreateFlight(String[] args) {
    int flightNumber;
    try {
      flightNumber = parseInt(args[0]);
    } catch(NumberFormatException ex) {
      throw new InvalidFlightNumberException(args[0]);
    }
    String source = parseAirportCode(args[1]);
    String departDate = parseDate(args[2]);
    String departTime = parseTime(args[3]);
    String destination = parseAirportCode(args[4]);
    String arriveDate = parseDate(args[5]);
    String arriveTime = parseTime(args[6]);

    return new Flight(flightNumber, source, departDate,departTime,destination,arriveDate, arriveTime);
  }

  /**
   * Traverses a string and checks if each character in the string is a letter.
   * @param string
   *        Airport code string
   * @return
   *        Returns valid 3-letter airport code.
   */
  static String parseAirportCode(String string) {
    if (string == null || string.length() != 3) {
      throw new InvalidAirportCodeException(string);
    }

    for (int i = 0; i < string.length(); i++) {
      char c = string.charAt(i);
      if (!(c >= 'A' && c <= 'Z') && !(c >= 'a' && c <= 'z')) {
        throw new InvalidAirportCodeException(string);
      }
    }
    return string.toUpperCase();
  }

  /**
   * The parseDate method parses dates, verifying the format is mm/dd/yyyy or m/d/yyy (months and days can include or omit the leading zero).
   * If the date is properly formatted, the date string is returned. If it is a not exception with a descriptive error
   * message is thrown.
   * @param dateString
   *        A string to be parsed (that is hopefully a properly formatted date)
   * @return
   *        Returns dateString if it is a valid date in a valid format
   * @throws InvalidDateException
   *        Exception is thrown for dates that do not exist and dates that are not properly formatted.
   */
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

  /**
   * The parseTime method takes a string and returns it if it is valid a valid time. If it is not, an error is thrown.
   * @param timeString
   *        String in 24-hour time
   * @return
   *        Returns time string if is a properly formatted, valid 24-hour time.
   */
  static String parseTime(String timeString) {
    StringTokenizer stringTokenizer = new StringTokenizer(timeString, ":");
    
    if(stringTokenizer.countTokens() != 2) {
      throw new InvalidTimeException(timeString);
    }

    int hour, minute;
    try{
      hour = Integer.parseInt(stringTokenizer.nextToken());
      minute = Integer.parseInt(stringTokenizer.nextToken());
    } catch (NumberFormatException ex) {
      throw new InvalidTimeException(timeString);
    }
    if((hour < 0 || hour > 24) || (minute < 0 || minute > 60)) {
      throw new InvalidTimeException(timeString);
    }

    return timeString;
  }

  /**
   * @return contents of README.txt as String
   */
  public static String getReadMe() throws IOException{
    StringBuilder readMeText = new StringBuilder();
    String line;
    InputStream readme = Project2.class.getResourceAsStream("README.txt");
    assert readme != null;
    BufferedReader reader = new BufferedReader(new InputStreamReader(readme));

    while((line = reader.readLine()) != null) {
      readMeText.append(line);
      readMeText.append("\n");
    }

    return readMeText.toString();
  }

  /**
   * The main method of the Project1 class checks for commandline arguments and kicks off the program by calling
   * checkOptions. It also catches most of the exceptions that might be thrown by other
   * methods and facilitates a graceful exit with helpful error messages.
   * @param args
   *        arguments from the commandline
   */
  public static void main(String[] args) {
    Flight flight = new Flight();

    if (args.length == 0) {
      printErrorMessageAndExit("Missing command line arguments." + printCommandLineInterfaceDescription());
    }
    try {
      checkOptions(args);
    } catch (IOException ex) {
      printErrorMessageAndExit("README file was not be found.");
    } catch (InvalidFlightNumberException ex) {
      printErrorMessageAndExit("Flight number " + ex.getInvalidFlightNumber() + " is not an integer." + printCommandLineInterfaceDescription());
    } catch (InvalidAirportCodeException ex) {
      printErrorMessageAndExit(ex.getInvalidAirportCode() + " is not a 3 letter airport code." + printCommandLineInterfaceDescription());
    } catch (InvalidDateException ex) {
      printErrorMessageAndExit(ex.getInvalidDate() + " is not a valid date." + printCommandLineInterfaceDescription());
    } catch (InvalidTimeException ex) {
      printErrorMessageAndExit(ex.getInvalidTime() + " is not a valid time" + printCommandLineInterfaceDescription());
    }


    System.exit(1);
  }

  /**
   * The printErrorMessageAndExit method writes messages (usually from exceptions) to standard error, and calls exit.
   * @param message
   *        Error message (usually from an exception thrown somewhere else in the program).
   */
  private static void printErrorMessageAndExit(String message) {
    System.err.println(message);
    System.exit(1);
  }

  /**
   * The printCommandLineInterfaceDescription method just prints a well-formatted description of the order and meaning
   * of the command line arguments. It is usually called following an exception that is thrown due to improperly
   * formatted arguments.
   * @return
   *        Returns a formatted string.
   */
  private static String printCommandLineInterfaceDescription() {
    return "\n" +
           "\n" +
           "args are (in this order): [options] <args>\n" +
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

class InvalidAirportCodeException extends RuntimeException {
    private final String invalidAirportCode;

    public InvalidAirportCodeException(String invalidAirportCode) { this.invalidAirportCode = invalidAirportCode;
    }

    public String getInvalidAirportCode() {
      return invalidAirportCode;
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

