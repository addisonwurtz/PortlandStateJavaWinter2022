package edu.pdx.cs410J.awurtz;

import edu.pdx.cs410J.AirportNames;
import edu.pdx.cs410J.ParserException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringTokenizer;

import static java.lang.Integer.*;
import static java.nio.file.Files.isReadable;
import static java.nio.file.Files.isWritable;

/**
 * The main class for the CS410J Airline Project
 */
public class Project4 {

  /**
   * The checkOptions method initially checks for -README, -print, or -textFile options in args, checks for the correct number of args, and then
   * directs the flow of the program appropriately.
   *
   * @param args
   *        The String of commandline arguments that will be parsed and, if they are correct, used to build a flight
   * @throws IOException is thrown if there is a problem accessing the readme file. The exception is caught in the main method.
   */
  public static Airline checkOptions(String[] args) throws IOException {
      Airline airlineFromFile, airlineFromCommandLine, prettyAirline = null;
      Flight flight;
      Path airlineFile;
      boolean print = false;
      boolean textFile = false;
      boolean prettyPrint = false;
      String fileName = null, prettyFileName = null;

      int i = 0;
      while (args[i].contains("-")) {
          switch (args[i]) {
              case "-README" -> {
                  System.out.println(getReadMe());
                  System.exit(1);
              }
              case "-print" -> print = true;

              case "-textFile" -> {
                  textFile = true;
                  fileName = args[++i];
              }
              case "-pretty" -> {
                  prettyPrint = true;
                  prettyFileName = args[++i];
              }
              default -> printErrorMessageAndExit(args[i] + " option is not supported.");
          }
          i++;
      }

      //check number of non-option commandline arguments
      if (args.length > (i + 10)) {
          printErrorMessageAndExit("Too many command line arguments.");
      }

      //creates airline and flight from commands line args
      airlineFromCommandLine = new Airline(args[i]);
      flight = parseArgsAndCreateFlight(args);

      //prints flight created with command line arguments
      if (print) {
          System.out.println(flight);
      }

      //reads and writes to supplied text file
      if (textFile) {
          //getValidFile returns null if file does not exist
          airlineFile = getValidFile(fileName);
          if (airlineFile == null) {
              airlineFromCommandLine.addFlight(flight);
              writeAirlineToFile(airlineFromCommandLine, Files.createFile(Path.of(fileName)));
              if(prettyPrint) {
                  prettyAirline = airlineFromCommandLine;
              }
              else {
                  return airlineFromCommandLine;
              }
          } else {
              airlineFromFile = attemptToReadFile(airlineFile);

              //check that airline in the same in file and from the new flight
              assert airlineFromFile != null;
              if (Objects.equals(airlineFromFile.getName(), airlineFromCommandLine.getName())) {
                  airlineFromFile.addFlight(flight);
                  writeAirlineToFile(airlineFromFile, airlineFile);
                  if(prettyPrint) {
                      prettyAirline = airlineFromFile;
                  }
                  else {
                      return airlineFromFile;
                  }
              } else {
                  throw new AirlineFromFileDoesNotMatchAirlineFromCommandLineException(airlineFromFile, airlineFromCommandLine);
              }
          }
      }

      //pretty prints airline
      if (prettyPrint) {
          if(!textFile) {
              prettyAirline = airlineFromCommandLine;
              prettyAirline.addFlight(flight);
          }
          prettyPrintAirline(prettyAirline, prettyFileName);
          return prettyAirline;
      }

          //creates flight in case of no options
      if (!print && !textFile && !prettyPrint){
              if (args.length > 10) {
                  printErrorMessageAndExit("Too many command line arguments.");
              }
              airlineFromCommandLine.addFlight(parseArgsAndCreateFlight(args));
              return airlineFromCommandLine;
      }
      return airlineFromCommandLine;
  }

    /**
     * checks if file exists
     * @param fileName name of file to be retrieved
     * @return file if it exists, otherwise null
     */
  static Path getValidFile(String fileName) {

      Path file = Paths.get(fileName);

      if(Files.exists(file)) {
          return file;
      }
      else {
          return null;
      }
  }

    /**
     * reads airline and flights from file
     * @param fileToRead file to read from
     * @return airline read from file, returns null if file is empty
     */
  static Airline attemptToReadFile(Path fileToRead) {
      TextParser parser;
      try {
          if (isReadable(fileToRead)) {
              parser = new TextParser(new FileReader(String.valueOf(fileToRead)));
              //parser = new TextParser(Files.newBufferedReader(fileToRead));
              return parser.parse();
          }
      } catch (ParserException ex) {
          printErrorMessageAndExit(ex.getMessage());
      } catch(IOException ex) {
         printErrorMessageAndExit("There was an issue opening " + fileToRead);
      } catch (SecurityException ex) {
          printErrorMessageAndExit("You do not have permission to read this file: " + fileToRead);
      }
      return null;
  }

    /**
     * writes airline and associated flights to file
     * @param airline to be written to the file
     * @param fileToWrite file to write airline in
     */
  static void writeAirlineToFile(Airline airline, Path fileToWrite) {
      TextDumper writer;
      try {
          if(isWritable(fileToWrite)) {
             writer = new TextDumper(Files.newBufferedWriter(fileToWrite));
             writer.dump(airline);
          }
      } catch (IOException ex) {
         printErrorMessageAndExit("There was a problem while writing to " + fileToWrite.getFileName());
      }
  }

    /**
     * Pretty prints to specified file or standard out (if fileName is "-");
     * @param airline to be written
     * @param fileName name of file to pretty print airline
     */
  static void prettyPrintAirline(Airline airline, String fileName) {
      Path prettyFile = getValidFile(fileName);
      PrettyPrinter prettyPrinter;

      try {
          if (fileName.equals("-")) {
              //pretty print to standard out
              prettyPrinter = new PrettyPrinter(new OutputStreamWriter(System.out));
              System.out.println();
              prettyPrinter.dump(airline);
              System.out.println();
          } else {
              if (prettyFile == null) {
                  prettyFile = Files.createFile(Path.of(fileName));
              }
              //Pretty print to file
              prettyPrinter = new PrettyPrinter(new FileWriter(prettyFile.toFile()));
          }
          prettyPrinter.dump(airline);
      } catch (IOException ex) {
          printErrorMessageAndExit("There was a problem while pretty printing to " + fileName);
      }
  }

  /**
   * The parseArgsAndCreateFlight method is called in order to individually check that each of the args is properly
   * formatted and, if they are,
   * returns a flight built from the freshly parsed parameters.
   * @param args
   *        The commandline arguments (options are ignored in this method)
   * @return
   *        Returns the freshly constructed flight.
   * @throws MissingCommandLineArgumentException that specifies which argument is missing
   */
 static Flight parseArgsAndCreateFlight(String[] args) {
     String[] flightArgs;
     int i = 0, j = 0;
     int flightNumber;
     String source, departDate, departTime, destination, arriveDate, arriveTime;

     while(args[i].contains("-")) {
         if(args[i].equals("-textFile")) { ++i; }
         ++i;
     }
     flightArgs = Arrays.copyOfRange(args, ++i, args.length);

     try {
         flightNumber = parseInt(flightArgs[j]);
    } catch(NumberFormatException ex) {
      throw new InvalidFlightNumberException(flightArgs[j]);
    } catch(ArrayIndexOutOfBoundsException ex) {
        throw new MissingCommandLineArgumentException("flight number");
    }
    try {
        source = parseAirportCode(flightArgs[++j]);
    } catch(ArrayIndexOutOfBoundsException ex) {
        throw new MissingCommandLineArgumentException("source airport code");
    }
    try {
        departDate = parseDate(flightArgs[++j]);
    } catch (ArrayIndexOutOfBoundsException ex) {
        throw new MissingCommandLineArgumentException("departure date");
    }
    try {
        departTime = parseTime(flightArgs[++j], flightArgs[++j]);
    } catch (ArrayIndexOutOfBoundsException ex) {
        throw new MissingCommandLineArgumentException("departure time");
    }
    try {
        destination = parseAirportCode(flightArgs[++j]);
    } catch (ArrayIndexOutOfBoundsException ex) {
        throw new MissingCommandLineArgumentException("destination airport");
    }
    try {
        arriveDate = parseDate(flightArgs[++j]);
    } catch (ArrayIndexOutOfBoundsException ex) {
        throw new MissingCommandLineArgumentException("arrival date");
    }
    try {
        arriveTime = parseTime(flightArgs[++j], flightArgs[++j]);
    } catch (ArrayIndexOutOfBoundsException ex) {
        throw new MissingCommandLineArgumentException("arrival time");
    }

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
    if(AirportNames.getName(string.toUpperCase()) == null) {
        throw new InvalidAirportCodeException(string);
    }
    return string.toUpperCase();
  }

  /**
   * The parseDate method parses dates, verifying the format is mm/dd/yyyy or m/d/yyy (months and days can include or
   * omit the leading zero).
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
   *        String in 12-hour time
   * @param amPm
   *        String containing am or pm
   * @return
   *        Returns time string if is a properly formatted, valid 24-hour time.
   */
  static String parseTime(String timeString, String amPm) {
    StringTokenizer stringTokenizer = new StringTokenizer(timeString, ":");

    if(stringTokenizer.countTokens() != 2) {
      throw new InvalidTimeException(timeString + " is not a valid time. Times should be of the form hh:mm.");
    }

    int hour, minute;
    try{
      hour = Integer.parseInt(stringTokenizer.nextToken());
      minute = Integer.parseInt(stringTokenizer.nextToken());
    } catch (NumberFormatException ex) {
      throw new InvalidTimeException(timeString + " is not a valid time.");
    }
    if((hour < 1 || hour > 12) || (minute < 0 || minute > 60)) {
      throw new InvalidTimeException(timeString + " is not a valid 12-hour time.");
    }
    if(amPm.equalsIgnoreCase("am") || amPm.equalsIgnoreCase("pm")) {
        return timeString + " " + amPm;
    }
    else {
        throw new InvalidTimeException("The time " + timeString + " must be followed by \"am\" or \"pm\". You entered "
        + amPm);
    }

  }

  /**
   * @return contents of README.txt as String
   */
  public static String getReadMe() {

    StringBuilder readMeText = new StringBuilder();
    String line;
    InputStream readme = Project4.class.getResourceAsStream("README.txt");
    assert readme != null;
    BufferedReader reader = new BufferedReader(new InputStreamReader(readme));

    try {
        while ((line = reader.readLine()) != null) {
            readMeText.append(line);
            readMeText.append("\n");
        }
    } catch (IOException e) {
        printErrorMessageAndExit("There was a problem reading README.txt");
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
    //Flight flight = new Flight();
    Airline airline;

    if (args.length == 0) {
        printErrorMessageAndExit("Missing command line arguments." + printCommandLineInterfaceDescription());
    }
    try {
        airline = checkOptions(args);
    } catch(MissingCommandLineArgumentException ex) {
      printErrorMessageAndExit("Missing " + ex.getMissingArgument());
    } catch (IOException ex) {
        ex.printStackTrace();
      printErrorMessageAndExit(ex.getMessage());
    } catch (InvalidFlightNumberException ex) {
      printErrorMessageAndExit("Flight number " + ex.getInvalidFlightNumber() + " is not an integer." + printCommandLineInterfaceDescription());
    } catch (InvalidAirportCodeException ex) {
      printErrorMessageAndExit(ex.getInvalidAirportCode() + " is not a valid airport code." + printCommandLineInterfaceDescription());
    } catch (InvalidDateException ex) {
      printErrorMessageAndExit(ex.getInvalidDate() + " is not a valid date." + printCommandLineInterfaceDescription());
    } catch (InvalidTimeException | InvalidDepartureException | InvalidArrivalException ex) {
      printErrorMessageAndExit(ex.getMessage());
    } catch (AirlineFromFileDoesNotMatchAirlineFromCommandLineException ex) {
        printErrorMessageAndExit("Airlines doe not match.\nAirline from file: " + ex.getAirlineFromFile() +
                                 "\nAirline from command line: " + ex.getAirlineFromCommandLine());
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
    return """


            args are (in this order): [options] <args>
            \tairline                   The name of the airline
            \tflightNumber              The flight number
            \tsrc                       Three-letter code of departure airport
            \tdepart                    Departure date time (am/pm)
            \tdest                      Three-letter code of arrival airport
            \tarrive                    Arrival date time (am/pm)
            options are (options may appear in any order):
            \t-textFile file            Where to read/write the airline info
            \t-print                    Prints a description of the new flight
            \t-README                   Prints a README for this project and exits
            Date and time should be in the format: mm/dd/yyy hh:mm
            """;

  }
}

class MissingCommandLineArgumentException extends RuntimeException {
    private final String missingArgument;

    public MissingCommandLineArgumentException(String missingArgument) {
        this.missingArgument = missingArgument;
    }

    public String getMissingArgument() {
        return missingArgument;}
}

class InvalidFlightNumberException extends NumberFormatException {
  private final String invalidFlightNumber;

  public InvalidFlightNumberException(String invalidFlightNumber) {
      this.invalidFlightNumber = invalidFlightNumber;}

  public String getInvalidFlightNumber() {

      return invalidFlightNumber;
  }
}

class InvalidAirportCodeException extends RuntimeException {
    private final String invalidAirportCode;

    public InvalidAirportCodeException(String invalidAirportCode) {
        this.invalidAirportCode = invalidAirportCode;
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

    /**
     * Exception constructor
     * @param airlineFromFile name of airline in text file
     * @param airlineFromCommandLine name of airline from the commandline.
     */
    public AirlineFromFileDoesNotMatchAirlineFromCommandLineException(Airline airlineFromFile,
                                                                      Airline airlineFromCommandLine) {
        this.airlineFromFile = airlineFromFile;
        this.airlineFromCommandLine = airlineFromCommandLine;
    }

    /**
     * @return name of airline from text file
     */
    public String getAirlineFromFile() {
        return airlineFromFile.getName();
    }

    /**
     * @return name of airline from commandline
     */
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

