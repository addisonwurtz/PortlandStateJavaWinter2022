package edu.pdx.cs410J.awurtz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

import static java.lang.Integer.parseInt;

/**
 * The main class for the CS410J airline Project
 */
public class Project1 {

  public static void parseCommandLineArguments(String[] args) {
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
    } catch (NumberFormatException ex) {
      printErrorMessageAndExit("Flight number was not an integer.");
    }
    String source = args[3];
    if (source.length() > 3 || !isAlpha(source)) {
      printErrorMessageAndExit("Source is not a 3 letter airport code.");
    }
    String departTime = args[4];
    String departDate = args[5];
    String destination = args[6];
    String arriveDate = args[7];
    String arriveTime = args[8];

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

  private static void printErrorMessageAndExit(String message) {
    System.err.println(message);
    System.exit(1);
  }


  public static void main(String[] args) {
    Flight flight = new Flight();  // Refer to one of Dave's classes so that we can be sure it is on the classpath

    if (args.length < 9) {
      System.err.println("Missing command line arguments.");
      System.out.println("Arguments should be as follows:");
      System.out.println("airline_name flight_number departure_airport_code departure_date departure_time destination_airport_code arrival_date arrival_time");
      System.out.println("Options are: -print (prints the description of the new flight)");
      System.out.println("             -README (Prints a README for this project and exits)");
    } else {
      parseCommandLineArguments(args);
    }

    System.exit(1);
  }
}

