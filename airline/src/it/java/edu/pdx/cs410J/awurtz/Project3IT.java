package edu.pdx.cs410J.awurtz;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * An integration test for the {@link Project3} main class.
 */
class Project3IT extends InvokeMainTestCase {

  /**
   * Invokes the main method of {@link Project3} with the given arguments.
   */
  private MainMethodResult invokeMain(String... args) {
    return invokeMain(Project3.class, args);
  }

  /**
   * Tests that invoking the main method with no arguments issues an error
   */
  @Test
  void testNoCommandLineArguments() {
    MainMethodResult result = invokeMain();
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing command line arguments"));
  }

  @Test
  void parserDetectsTooManyCommandLineArguments() {
    String[] args = new String[]{"-print", "Delta", "99", "PDX", "11/11/2011", "11:11", "am", "SFO", "12/12/2012",
            "12:12", "pm", "behind schedule"};
    MainMethodResult result = invokeMain(args);
    assertThat(result.getTextWrittenToStandardError(), containsString("Too many command line arguments"));

  }


  @Test
  void testNotEnoughCommandLineArguments() {
    String[] args = new String[]{"-print", "Delta", "99", "PDX", "11/11/2011", "11:11", "am", "SFO", "12/12/2012", "pm"};
    MainMethodResult result = invokeMain(args);
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing arrival time"));
  }

  @Test
  void parserDetectsNonIntegerFlightNumber() {
    String[] args = new String[]{"-print", "Delta", "u9", "PDX", "11/11/2011", "11:11", "SFO", "12/12/12", "12:12"};
    MainMethodResult result = invokeMain(args);
    assertThat(result.getTextWrittenToStandardError(), containsString("Flight number " + args[2] +
                                                                      " is not an integer."));
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  void parserDetectsInvalidSource() {
    String[] args = new String[]{"-print", "Delta", "99", "PDX9", "11/11/2011", "11:11", "SFO", "12/12/12", "12:12"};
    MainMethodResult result = invokeMain(args);
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString(" is not a 3 letter airport code."));

  }

  @Test
  void parserDetectsInvalidDepartureDate() {
    String[] args = new String[]{"-print", "Delta", "99", "PDX", "11/11/11", "11:11", "SFO", "12/12/12", "12:12"};
    MainMethodResult result = invokeMain(args);
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString(" is not a valid date."));
  }

  @Test
  void errorWhenFlightNumberIsNotAnInteger() {
    String[] args = new String[]{"-print", "Delta", "NUMBER", "PDX", "03/03/2022", "12:00", "ORD", "03/03/2022", "16:00"};
    MainMethodResult result = invokeMain(args);
    assertThat(result.getExitCode(), equalTo(1));

    assertThat(result.getTextWrittenToStandardError(), containsString("is not an integer"));
  }

  @Test
  void errorForInvalidDepartureTime() {
    String[] args = new String[]{"Delta", "123", "PDX", "03/03/2022", "12:XX", "pm", "ORD", "03/03/2022", "16:00", "pm"};
    MainMethodResult result = invokeMain(args);
    assertThat(result.getExitCode(),equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("is not a valid time"));
  }

  @Test
  void errorForInvalidArrivalDate() {
    String[] args = new String[]{"Delta", "123", "PDX", "03/03/2022", "12:00", "pm", "ORD", "03/03/20/22", "4:00", "pm"};
    MainMethodResult result = invokeMain(args);
    assertThat(result.getExitCode(),equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("is not a valid date."));
  }

  @Test
  void errorForUnsupportedOption() {
    String[] args = new String[]{"-fred", "Delta", "123", "PDX", "03/03/2022", "12:00", "ORD", "03/03/20/22", "16:00"};
    MainMethodResult result = invokeMain(args);
    assertThat(result.getExitCode(),equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("option is not supported."));
  }

  @Test
  void errorForExtraCommandLineArgument() {
    String[] args = new String[]{"Delta", "123", "PDX", "03/03/2022", "12:00", "pm", "ORD", "03/03/2022", "4:00", "pm",
            "fred"};
    MainMethodResult result = invokeMain(args);
    assertThat(result.getExitCode(),equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Too many command line arguments."));
  }

  @Test
  void errorForMissingFlightNumber() {
    String[] args = new String[]{"Delta"};
    MainMethodResult result = invokeMain(args);
    assertThat(result.getExitCode(),equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing flight number"));
  }

  @Test
  void errorForMissingSourceAirport() {
    String[] args = new String[]{"Delta", "99"};
    MainMethodResult result = invokeMain(args);
    assertThat(result.getExitCode(),equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing source airport"));
  }

  @Test
  void errorForMissingDepartureDate() {
    String[] args = new String[]{"Delta", "99", "PDX"};
    MainMethodResult result = invokeMain(args);
    assertThat(result.getExitCode(),equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing departure date"));
  }

  @Test
  void errorForMissingDepartureTime() {
    String[] args = new String[]{"Delta", "99", "PDX", "12/12/2022"};
    MainMethodResult result = invokeMain(args);
    assertThat(result.getExitCode(),equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing departure time"));
  }

  @Test
  void errorForMissingDestinationAirport() {
    String[] args = new String[]{"Delta", "99", "PDX", "12/12/2022", "12:12", "am"};
    MainMethodResult result = invokeMain(args);
    assertThat(result.getExitCode(),equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing destination airport"));
  }

  @Test
  void errorForMissingArrivalDate() {
    String[] args = new String[]{"Delta", "99", "PDX", "12/12/2022", "12:12", "am", "SFO"};
    MainMethodResult result = invokeMain(args);
    assertThat(result.getExitCode(),equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing arrival date"));
  }

  @Test
  void errorForMissingArrivalTime () {
    String[] args = new String[]{"Delta", "123", "PDX", "03/03/2022", "12:00", "am", "ORD", "03/03/2022"};
    MainMethodResult result = invokeMain(args);
    assertThat(result.getExitCode(),equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing arrival time"));
  }

  @Test
  void readMeOptionPrintsReadMe () {
    String[] args = new String[] {"-README"};
    MainMethodResult result = invokeMain(args);
    assertThat(result.getExitCode(),equalTo(1));
    assertThat(result.getTextWrittenToStandardOut(), containsString("Addison Wurtz"));
  }

  @Test
  void printOptionExitsWithErrorForTooManyArgs () {
    String[] args = new String[] {"-print", "Delta", "123", "PDX", "03/03/2022", "12:00", "am", "ORD", "03/03/2022",
            "4:00", "pm", "extra arg"};
    MainMethodResult result = invokeMain(args);
    assertThat(result.getExitCode(),equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Too many command line arguments"));
  }

  @Test
  void printOptionPrintsFlightString () {
    String[] args = new String[] {"-print", "Delta", "123", "PDX", "03/03/2022", "12:00", "am", "ORD", "03/03/2022",
            "4:00", "pm"};
    MainMethodResult result = invokeMain(args);
    assertThat(result.getExitCode(),equalTo(1));
    assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 123 departs PDX at 3/3/22 12:00 PM"
            + " arrives ORD at 3/3/22 4:00 AM"));

  }
}
