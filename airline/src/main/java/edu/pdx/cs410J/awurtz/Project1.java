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

  /**
   * The checkOptionsAndParseCommandLineArguments method initially checks for -README or -print options in args, checks for the correct number of args, and then
   * directs the flow of the program appropriates.
   *
   * @param args
   *        The String of commandline arguments that will be parsed and, if they are correct, used to build a flight
   * @throws IOException
   *         IOException is thrown if there is a problem accessing the readme file. The exception is caught in the main method.
   */
  public static void checkOptionsAndParseCommandLineArguments(String[] args) throws IOException {
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

  /**
   * The parseArgsAndCreateFlight method is called in order to individually check that each of the args is properly formatted and, if they are,
   * returns a flight built from the freshly parsed parameters.
   * @param args
   *        The commandline arguments (options are ignored in this method)
   * @return
   *        Returns the freshly constructed flight.
   */
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

    return new Flight(flightNumber, source, departDate,departTime,destination,arriveDate, arriveTime);

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
   * The isAlpha method traverses a string and checks if each character in the string is a letter.
   * @param string
   *        A string
   * @return
   *        Returns false if any of the characters are not a letter. Returns true when all characters are letters.
   */
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

  /**
   * The main method of the Project1 class checks for commandline arguments and kicks off the program by calling
   * checkOptionsAndParseCommandLineArguments. It also catches most of the exceptions that might be thrown by other
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
      checkOptionsAndParseCommandLineArguments(args);
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

