package edu.pdx.cs410J.awurtz;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * An integration test for the {@link Project1} main class.
 */
class Project1IT extends InvokeMainTestCase {

    /**
     * Invokes the main method of {@link Project1} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {
        return invokeMain( Project1.class, args );
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
  void testNotEnoughCommandLineArguments() {
    String[] args = new String[] {"-print", "Delta", "u9", "PDX", "SFO", "11/11/11 11:11", "12/12/12 12:12"};
    MainMethodResult result = invokeMain(args);
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing command line arguments"));
  }

  @Test
    void parserDetectsNonIntegerFlightNumber() {
      String[] args = new String[] {"-print", "Delta", "u9", "PDX", "SFO", "11/11/11", "11:11", "12/12/12", "12:12"};
      MainMethodResult result = invokeMain(args);
      //How are you supposed to use assertThrows?
      //I am trying to say "Assert that createFlightAndPrintDescription throws a NumberFormatException when given the above String[] args.
      //assertThrows(NumberFormatException.class, () -> Project1.createFlightAndPrintDescription(args));
      assertThat(result.getTextWrittenToStandardError(), containsString("Flight number is not an integer."));
      assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
    void parserDetectsInvalidSource() {
      String[] args = new String[] {"-print", "Delta", "99", "PDX9", "SFO", "11/11/11", "11:11", "12/12/12", "12:12"};
      MainMethodResult result = invokeMain(args);
      assertThat(result.getExitCode(), equalTo(1));
      assertThat(result.getTextWrittenToStandardError(), containsString(" is not a 3 letter airport code."));

  }

  @Test
  void parserDetectsInvalidDepartureDate() {
    String[] args = new String[] {"-print", "Delta", "99", "PDX", "SFO", "11/11/11", "11:11", "12/12/12", "12:12"};
    MainMethodResult result = invokeMain(args);
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString(" is not a valid date."));
  }


}