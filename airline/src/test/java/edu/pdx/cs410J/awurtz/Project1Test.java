package edu.pdx.cs410J.awurtz;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static edu.pdx.cs410J.awurtz.Project1.parseDate;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A unit test for code in the <code>Project1</code> class.  This is different
 * from <code>Project1IT</code> which is an integration test (and can handle the calls
 * to {@link System#exit(int)} and the like.
 */
class Project1Test {

  @Test
  void readmeCanBeReadAsResource() throws IOException {
    try (
      InputStream readme = Project1.class.getResourceAsStream("README.txt")
    ) {
      assertThat(readme, not(nullValue()));
      BufferedReader reader = new BufferedReader(new InputStreamReader(readme));
      String line = reader.readLine();
      assertThat(line, containsString("This is a README file!"));
    }
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
    assertThrows(InvalidDateException.class, () -> Project1.parseDate(date));
  }

  @Test
  void parseDateThrowsInvalidDateExceptionForFebruaryThirtiethDate() {
    String date = "02/30/2022";
    assertThrows(InvalidDateException.class, () -> Project1.parseDate(date));
  }

  @Test
  void parseDateThrowsInvalidDateExceptionForAprilThirtyFirstDate() {
    String date = "4/31/2022";
    assertThrows(InvalidDateException.class, () -> Project1.parseDate(date));
  }

  @Test
  void parseTimeDetectsValidTime() {
    String time = "11:11";
    assertThat(Project1.parseTime(time), equalTo(time));
  }

}
