package edu.pdx.cs410J.awurtz;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static edu.pdx.cs410J.awurtz.Project2.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

//java -jar target/airline-2022.0.0.jar -README
/**
 * A unit test for code in the <code>Project1</code> class.  This is different
 * from <code>Project1IT</code> which is an integration test (and can handle the calls
 * to {@link System#exit(int)} and the like.
 */
class Project2Test {

  @Test
  void readmeCanBeReadAsResource() throws IOException {
    try (
      InputStream readme = Project2.class.getResourceAsStream("README.txt")
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
    assertThrows(InvalidAirportCodeException.class, () -> Project2.parseAirportCode(code));
  }

  @Test
  void parseAirportCodeReturnsValidCodeInUppercase() {
    String code = "msp";
    assertThat(parseAirportCode(code), equalTo(code.toUpperCase()));
  }

  @Test
  void parserDetectsValidDateFormat() {
    String date = "02/20/2022";
    assertThat(parseDate(date), equalTo(date));
  }

  @Test
  void parseDateDetectsValidDateWithSingleDigitMonth() {
    String date = "1/23/2022";
    assertThat(parseDate(date), equalTo(date));
  }

  @Test
  void parseDateDetectsValidDateWithSingleDigitDay() {
    String date = "12/3/2022";
    assertThat(parseDate(date), equalTo(date));
  }

  @Test
  void parseDateThrowsInvalidDateExceptionForTwoDigitYear() {
    String date = "12/15/21";
    assertThrows(InvalidDateException.class, () -> Project2.parseDate(date));
  }

  @Test
  void parseDateThrowsInvalidDateExceptionForFebruaryThirtiethDate() {
    String date = "02/30/2022";
    assertThrows(InvalidDateException.class, () -> Project2.parseDate(date));
  }

  @Test
  void parseDateThrowsInvalidDateExceptionForAprilThirtyFirstDate() {
    String date = "4/31/2022";
    assertThrows(InvalidDateException.class, () -> Project2.parseDate(date));
  }

  @Test
  void parseTimeDetectsValidTime() {
    String time = "11:11";
    assertThat(Project2.parseTime(time), equalTo(time));
  }

  @Test
  void parseDateThrowsInvalidDateExceptionForInvalidDate() {
   String date = "13/12/2012";
   assertThrows(InvalidDateException.class, () -> Project2.parseDate(date));
  }

  @Test
  void parseArgsAndCreateFlightThrowsInvalidSourceExceptionIfSourceIsTooNotThreeCharactersLong() {
    String[] args = new String[] {"Delta", "99", "PDXPDX", "11/11/2011", "11:11", "SFO", "12/12/12", "12:12"};
    assertThrows(InvalidAirportCodeException.class, () -> Project2.parseArgsAndCreateFlight(args));
  }

  @Test
  void parseArgsAndCreateFlightThrowsInvalidSourceExceptionIfSourceIsNotAlphabetic() {
    String[] args = new String[] {"99", "999", "11/11/2011", "11:11", "SFO", "12/12/12", "12:12"};
    assertThrows(InvalidAirportCodeException.class, () -> Project2.parseArgsAndCreateFlight(args));
  }

  @Test
  void parseArgsAndReturnFlightReturnsFlight() {
    String[] args = new String[] {"Delta", "99", "PDX", "11/11/2011", "11:11", "SFO", "12/12/2012", "12:12"};
    Flight flight = new Flight(99, "PDX", "11/11/2011", "11:11", "SFO", "12/12/2012", "12:12");
    assertThat(Project2.parseArgsAndCreateFlight(args).destination, equalTo(flight.getDestination()));
  }

  @Test
  void parseTimeThrowsInvalidTimeExceptionForIncompleteTime() {
    String time = "12";
    assertThrows(InvalidTimeException.class, () -> Project2.parseTime(time));
  }

  @Test
  void attemptToReadFileReturnsNewForFileThatDoesNotExist() throws IOException {
    String fileName = "fakeFile.txt";
    assertThat(Project2.getValidFile(fileName), equalTo(null));
  }

  @Test
  void attemptToReadFileReturnsFileForFileThatDoesExistWithTxt() throws IOException {
    String fileName = "realFile.txt";
    Path file = Files.createFile(Path.of(fileName));

    assertThat(Files.exists(Project2.getValidFile(fileName)), equalTo(true));
    Files.delete(file);
  }

  @Test
  void attemptToReadFileReturnsFileForFileThatDoesExist() throws IOException {
    String fileName = "fileDoesNotYetExist.txt";
    Path file = Files.createFile(Path.of(fileName));

    assertThat(Files.exists(Project2.getValidFile(fileName)), equalTo(true));
    Files.delete(file);
  }

  @Test
  void getValidFileReturnsReadableFile() throws IOException {
    String fileName = "fakeFile.txt";
    assertThat(Project2.getValidFile(fileName), equalTo(null));
  }

  @Test
  void checkOptionsReturnsAirlineBasedOnArgs() throws IOException {
    String[] args = new String[] {"Delta", "99", "PDX", "11/11/2011", "11:11", "SFO", "12/12/2012", "12:12"};
    assertThat(Project2.checkOptions(args).getName(), equalTo("Delta"));
  }

  @Disabled
  @Test
  void checkOptionsCorrectlyReadsTextFileAndReturnsUpdatedAirline() throws IOException {
    String[] args = new String[] {"-textFile", "AirlineTestFile", "Delta", "99", "PDX", "11/11/2011", "11:11", "SFO",
            "12/12/2012", "12:12"};
     assertThat(Project2.checkOptions(args).toString(), containsString("Delta with 2 flights"));
  }

  @Test
  void checkOptionsReturnsNewAirlineWhenGiveFileThatDoesNotExitYet() throws IOException {
    String[] args = new String[]{"-textFile", "NewAirlineFile", "Alaska", "99", "PDX", "11/11/2011", "11:11", "SFO",
            "12/12/2012", "12:12"};
    assertThat(Project2.checkOptions(args).getName(), equalTo("Alaska"));
    assertThat(Files.exists(Paths.get("NewAirlineFile")), equalTo(true));
    Files.delete(Paths.get("NewAirlineFile"));
  }

  @Disabled
  @Test
  void multipleFlightsAreReturnedWhenNewFlightIsAdded() throws IOException {
    String[] args = new String[]{"-textFile", "AirlineTestFile", "Delta", "99", "MSP", "1/27/2022", "15:00", "LAX",
            "1/27/2022", "19:25"};
    assertThat(Project2.checkOptions(args).toString(), containsString("2 flights"));
  }

  @Test
  void checkOptionsThrowsErrorWhenNewAirlineDoesNotMatchAirlineInFile() {
    String[] args = new String[]{"-textFile", "AirlineTestFile", "Alaska", "99", "MSP", "1/27/2022", "15:00", "LAX",
            "1/27/2022", "19:25"};
    assertThrows(AirlineFromFileDoesNotMatchAirlineFromCommandLineException.class, () -> checkOptions(args));

  }

  @Disabled
  @Test
  void threeFlightsAreReturnedWhenNewFlightIsAdded() throws IOException {
    String[] args = new String[]{"-textFile", "AirlineTestFile", "Delta", "102", "LAX", "5/13/2022", "20:00", "JFK",
            "5/14/2022", "02:25"};
    assertThat(Project2.checkOptions(args).toString(), containsString("3 flights"));
  }
}
