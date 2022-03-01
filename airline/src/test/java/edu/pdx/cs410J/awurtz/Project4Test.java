package edu.pdx.cs410J.awurtz;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static edu.pdx.cs410J.awurtz.Project4.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

//java -jar target/airline-2022.0.0.jar -README
/**
 * A unit test for code in the <code>Project1</code> class.  This is different
 * from <code>Project1IT</code> which is an integration test (and can handle the calls
 * to {@link System#exit(int)} and the like.
 */
class Project4Test {

  @Test
  void readmeCanBeReadAsResource() throws IOException {
    try (
      InputStream readme = Project4.class.getResourceAsStream("README.txt")
    ) {
      assertThat(readme, not(nullValue()));
      BufferedReader reader = new BufferedReader(new InputStreamReader(readme));
      String line = reader.readLine();
      assertThat(line, containsString("Addison Wurtz"));
    }
  }

  @Test
  void parseAirportCodeDetectsInvalidCode() {
    String code = "PDXX";
    assertThrows(InvalidAirportCodeException.class, () -> Flight.parseAirportCode(code));
  }


  @Test
  void parserDetectsValidDateFormat() {
    String date = "02/20/2022";
    assertThat(Flight.parseDate(date), equalTo(date));
  }

  @Test
  void parseDateDetectsValidDateWithSingleDigitMonth() {
    String date = "1/23/2022";
    assertThat(Flight.parseDate(date), equalTo(date));
  }

  @Test
  void parseDateDetectsValidDateWithSingleDigitDay() {
    String date = "12/3/2022";
    assertThat(Flight.parseDate(date), equalTo(date));
  }

  @Test
  void parseDateThrowsInvalidDateExceptionForTwoDigitYear() {
    String date = "12/15/21";
    assertThrows(InvalidDateException.class, () -> Flight.parseDate(date));
  }

  @Test
  void parseDateThrowsInvalidDateExceptionForFebruaryThirtiethDate() {
    String date = "02/30/2022";
    assertThrows(InvalidDateException.class, () -> Flight.parseDate(date));
  }

  @Test
  void parseDateThrowsInvalidDateExceptionForAprilThirtyFirstDate() {
    String date = "4/31/2022";
    assertThrows(InvalidDateException.class, () -> Flight.parseDate(date));
  }

  @Test
  void parseTimeDetectsValidTime() {
    String time = "11:11";
    String amPm = "am";
    assertThat(Flight.parseTime(time, amPm), equalTo(time + " " + amPm));
  }

  @Test
  void parseDateThrowsInvalidDateExceptionForInvalidDate() {
   String date = "13/12/2012";
   assertThrows(InvalidDateException.class, () -> Flight.parseDate(date));
  }

  @Test
  void parseArgsAndCreateFlightThrowsInvalidSourceExceptionIfSourceIsTooNotThreeCharactersLong() {
    String[] args = new String[] {"Delta", "99", "PDXPDX", "11/11/2011", "11:11", "SFO", "12/12/12", "12:12"};
    assertThrows(InvalidAirportCodeException.class, () -> new Flight(args));
  }

  @Test
  void parseArgsAndCreateFlightThrowsInvalidSourceExceptionIfSourceIsNotAlphabetic() {
    String[] args = new String[] {"99", "999", "11/11/2011", "11:11", "SFO", "12/12/12", "12:12"};
    assertThrows(InvalidAirportCodeException.class, () -> new Flight(args));
  }

  @Test
  void parseArgsAndReturnFlightReturnsFlight() {
    String[] args = new String[] {"Delta", "99", "PDX", "11/11/2011", "11:11", "am",  "SFO", "12/12/2012", "12:12", "pm"};
    Flight flight = new Flight(99, "PDX", "11/11/2011", "11:11 am", "SFO",
            "12/12/2012", "12:12 pm");
    assertThat(new Flight(args).destination, equalTo(flight.getDestination()));
  }

  @Test
  void parseTimeThrowsInvalidTimeExceptionForIncompleteTime() {
    String time = "12";
    String amPm = "pm";
    assertThrows(InvalidTimeException.class, () -> Flight.parseTime(time, amPm));
  }

  @Test
  void attemptToReadFileReturnsNewForFileThatDoesNotExist() {
    String fileName = "fakeFile.txt";
    assertThat(Project4.getValidFile(fileName), equalTo(null));
  }

  @Test
  void attemptToReadFileReturnsFileForFileThatDoesExistWithTxt() throws IOException {
    String fileName = "realFile.txt";
    Path file = Files.createFile(Path.of(fileName));

    assertThat(Files.exists(Project4.getValidFile(fileName)), equalTo(true));
    Files.delete(file);
  }

  @Test
  void attemptToReadFileReturnsFileForFileThatDoesExist() throws IOException {
    String fileName = "fileDoesNotYetExist.txt";
    Path file = Files.createFile(Path.of(fileName));

    assertThat(Files.exists(Project4.getValidFile(fileName)), equalTo(true));
    Files.delete(file);
  }

  @Test
  void getValidFileReturnsReadableFile() {
    String fileName = "fakeFile.txt";
    assertThat(Project4.getValidFile(fileName), equalTo(null));
  }

  @Test
  void checkOptionsReturnsAirlineBasedOnArgs() throws IOException {
    String[] args = new String[] {"Delta", "99", "PDX", "11/11/2011", "11:11", "am", "SFO", "12/12/2012", "12:12", "pm"};
    assertThat(Project4.checkOptions(args).getName(), equalTo("Delta"));
  }

  @Disabled
  @Test
  void checkOptionsCorrectlyReadsTextFileAndReturnsUpdatedAirline() throws IOException {
    String[] args = new String[] {"-textFile", "AirlineTestFile", "Delta", "99", "PDX", "11/11/2011", "11:11", "am", "SFO",
            "12/12/2012", "12:12", "pm"};
     assertThat(Project4.checkOptions(args).toString(), containsString("Delta with 2 flights"));
  }

  @Test
  void checkOptionsReturnsNewAirlineWhenGiveFileThatDoesNotExitYet() throws IOException {
    String[] args = new String[]{"-textFile", "NewAirlineFile", "Alaska", "99", "PDX", "11/11/2011", "11:11", "am", "SFO",
            "12/12/2012", "12:12","pm"};
    assertThat(Project4.checkOptions(args).getName(), equalTo("Alaska"));
    assertThat(Files.exists(Paths.get("NewAirlineFile")), equalTo(true));
    Files.delete(Paths.get("NewAirlineFile"));
  }

  @Disabled
  @Test
  void multipleFlightsAreReturnedWhenNewFlightIsAdded() throws IOException {
    String[] args = new String[]{"-textFile", "AirlineTestFile", "Delta", "99", "MSP", "1/27/2022", "15:00", "LAX",
            "1/27/2022", "19:25"};
    assertThat(Project4.checkOptions(args).toString(), containsString("2 flights"));
  }

  @Test
  void checkOptionsThrowsErrorWhenNewAirlineDoesNotMatchAirlineInFile() {
    String[] args = new String[]{"-textFile", "AirlineTestFile", "Alaska", "99", "MSP", "1/27/2022", "3:00", "pm",
            "LAX", "1/27/2022", "7:25", "pm"};
    assertThrows(AirlineFromFileDoesNotMatchAirlineFromCommandLineException.class, () -> checkOptions(args));

  }

  @Disabled
  @Test
  void threeFlightsAreReturnedWhenNewFlightIsAdded() throws IOException {
    String[] args = new String[]{"-textFile", "AirlineTestFile", "Delta", "102", "LAX", "5/13/2022", "10:00",  "am", "JFK",
            "5/14/2022", "02:25", "pm"};
    assertThat(Project4.checkOptions(args).toString(), containsString("3 flights"));
  }

  @Test
  void prettyPrintAirlinePrintsToFile() throws IOException {
    Airline airline = new Airline("United");
    Flight flight1 = new Flight(456, "DEN", "3/1/2022", "6:53 pm", "LAX",
            "3/1/2022", "11:27 pm");
    Flight flight2 = new Flight(456, "DEN", "2/1/2022", "6:53 pm", "LAX",
            "2/1/2022", "11:27 pm");
    Flight flight3 = new Flight(102, "LAX", "5/13/2022", "10:00 am", "JFK",
            "5/14/2022", "02:25 pm");

    airline.addFlight(flight1);
    airline.addFlight(flight2);
    airline.addFlight(flight3);

    Project4.prettyPrintAirline(airline, "PrettyPrinterTest");

    File textFile = new File("PrettyPrinterTest");
    BufferedReader reader = new BufferedReader(new FileReader(textFile));
    assertThat(reader.readLine(), containsString("United"));
    reader.close();
    Files.delete(Paths.get(String.valueOf(textFile)));
  }
}
