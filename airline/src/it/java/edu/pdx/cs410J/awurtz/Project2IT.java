package edu.pdx.cs410J.awurtz;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * An integration test for the {@link Project2} main class.
 */
class Project2IT extends InvokeMainTestCase {

    /**
     * Invokes the main method of {@link Project2} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {
        return invokeMain( Project2.class, args );
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
    String[] args = new String[] {"-print", "Delta", "u9", "PDX", "SFO", "11/11/2011", "11:11", "12/12/12", "12:12", "behind schedule"};
    MainMethodResult result = invokeMain(args);
    assertThat(result.getTextWrittenToStandardError(), containsString("Too many command line arguments"));

  }
  @Test
  void testNotEnoughCommandLineArguments() {
    String[] args = new String[] {"-print", "Delta", "99", "PDX", "11/11/2011", "11:11", "SFO", "12/12/2012"};
    MainMethodResult result = invokeMain(args);
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing command line arguments"));
  }

  @Test
    void parserDetectsNonIntegerFlightNumber() {
      String[] args = new String[] {"-print", "Delta", "u9", "PDX", "11/11/2011", "11:11", "SFO", "12/12/12", "12:12"};
      MainMethodResult result = invokeMain(args);
      assertThat(result.getTextWrittenToStandardError(), containsString("Flight number " + args[2] + " is not an integer."));
      assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
    void parserDetectsInvalidSource() {
      String[] args = new String[] {"-print", "Delta", "99", "PDX9", "11/11/2011", "11:11", "SFO", "12/12/12", "12:12"};
      MainMethodResult result = invokeMain(args);
      assertThat(result.getExitCode(), equalTo(1));
      assertThat(result.getTextWrittenToStandardError(), containsString(" is not a 3 letter airport code."));

  }

  @Test
  void parserDetectsInvalidDepartureDate() {
    String[] args = new String[] {"-print", "Delta", "99", "PDX", "11/11/11", "11:11", "SFO", "12/12/12", "12:12"};
    MainMethodResult result = invokeMain(args);
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString(" is not a valid date."));
  }



}