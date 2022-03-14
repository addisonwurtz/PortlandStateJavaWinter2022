package edu.pdx.cs410J.awurtz;

import edu.pdx.cs410J.InvokeMainTestCase;
import edu.pdx.cs410J.UncaughtExceptionInMain;
import edu.pdx.cs410J.web.HttpRequestHelper.RestException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.MethodOrderer.MethodName;

/**
 * An integration test for {@link Project5} that invokes its main method with
 * various arguments
 */
@TestMethodOrder(MethodName.class)
class Project5IT extends InvokeMainTestCase {
    private static final String HOSTNAME = "localhost";
    private static final String PORT = System.getProperty("http.port", "8080");

    @Test
    void test0RemoveAllMappings() throws IOException {
        AirlineRestClient client = new AirlineRestClient(HOSTNAME, Integer.parseInt(PORT));
        client.removeAllAirlines();
    }

    @Test
    void test1NoCommandLineArguments() {
        MainMethodResult result = invokeMain(Project5.class);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.MISSING_ARGS));
    }

    @Test
    void test3NoFlightsThrowsAirlineRestException() {
        String airlineName = "Non-Existent Airline";

        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, airlineName);

        assertThat(result.getExitCode(), equalTo(1));
        //assertThat(result.getTextWrittenToStandardError(), containsString("not found"));
    }

    @Test
    void test4AddFlight() {
        String airlineName = "Test Airline";
        int flightNumber = 12345;

        Airline airline = new Airline(airlineName);
        airline.addFlight(new Flight(flightNumber, "SEA", "07/19/2022",
                "1:02 pm", "ORD", "07/19/2022", "6:22 pm"));

        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, airlineName,
                String.valueOf(flightNumber), "SEA", "07/19/2022", "1:02", "pm", "ORD", "07/19/2022", "6:22", "pm");
        assertThat(result.getTextWrittenToStandardError(), result.getExitCode(), equalTo(0));

        result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, airlineName);
        String out = result.getTextWrittenToStandardOut();
        StringWriter sw = new StringWriter();
        PrettyPrinter prettyPrinter = new PrettyPrinter(sw);
        prettyPrinter.dump(airline);
        String text = sw.toString();

        assertThat(out, out, containsString(PrettyPrinter.formatFlightNumber(flightNumber)));
        assertThat(out, out, containsString("SEA -> ORD"));

    }

    @Test
    void readmeOptionPrintsREADME() {
        MainMethodResult result = invokeMain(Project5.class, "-README");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Addison Wurtz"));
    }

    @Test
    void errorIfHostIsSpecifiedButPortIsNot() {
        MainMethodResult result = invokeMain(Project5.class, "-host", "localhost");
        assertThat(result.getTextWrittenToStandardError(),
                containsString("Missing port"));
    }

    @Test
    void errorIfPortIsSpecifiedButHostIsNot() {
        MainMethodResult result = invokeMain(Project5.class, "-port", "8080");
        assertThat(result.getTextWrittenToStandardError(),
                containsString("Missing host"));
    }

    @Test
    void searchOptionReturnsNoMatchingFlights() {
       invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "Test Airline",
               "345", "SEA", "07/19/2022", "1:02", "pm", "ORD", "07/19/2022", "6:22", "pm");

        String[] args = new String[] {"-search", "-port", "8080", "-host", "localhost", "Test Airline", "SFO", "MSP"};
        MainMethodResult result = invokeMain(Project5.class, args);

        assertThat(result.getTextWrittenToStandardOut(), containsString("There were no Test Airline flights from"));
    }

    @Test
    void searchForAirlineThatDoesNotExistThrowsHttpException() {
        String[] args = new String[] {"-search", "-port", "8080", "-host", "localhost", "Test Airline", "JFK", "JFK"};

        try {
            MainMethodResult result = invokeMain(Project5.class, args);
        } catch (UncaughtExceptionInMain ex) {
            RestException cause = (RestException) ex.getCause();
            assertThat(cause.getHttpStatusCode(), equalTo(HttpURLConnection.HTTP_NOT_FOUND));
        }
    }

    @Test
    void prettyPrintAllFlightsThatMatchSearchConditions() {
       MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "Delta",
                "345", "SEA", "07/19/2022", "1:02", "pm", "ORD", "07/19/2022", "6:22", "pm");
       assertThat(result.getExitCode(), equalTo(0));
       result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "Delta",
                "567", "SEA", "07/20/2022", "4:42", "pm", "ORD", "07/20/2022", "7:25", "pm");
        assertThat(result.getExitCode(), equalTo(0));
       result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "United",
                "666", "SEA", "02/09/2022", "4:42", "pm", "ORD", "02/09/2022", "9:25", "pm");
        assertThat(result.getExitCode(), equalTo(0));
       result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "Delta",
                "111", "PDX", "08/20/2023", "4:42", "pm", "ORD", "08/20/2023", "8:25", "pm");
        assertThat(result.getExitCode(), equalTo(0));

       result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "-search", "Delta", "SEA", "ORD");

       assertThat(result.getExitCode(), equalTo(0));
       assertThat(result.getTextWrittenToStandardOut(), containsString("SEA -> ORD"));
       assertThat(result.getTextWrittenToStandardOut(), containsString("Jul 19 2022"));
       assertThat(result.getTextWrittenToStandardOut(), containsString("Jul 20 2022"));
    }

    @Test
    void gracefulExitWithInvalidSourceForSearchOption() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "-search", "Delta",
                "345", "SEA");
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("not a valid airport code"));
    }

    @Test
    void gracefulExitWithInvalidDestinationForSearchOption() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "-search", "Delta",
                "sjd", "SEAA");
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("not a valid airport code"));
    }

    @Test
    void testThatFlightDurationIsCorrect() {
        MainMethodResult result = invokeMain(Project5.class, "-host", "localhost", "-port", "8080",
                "AirDave", "123", "PDX", "07/19/2022", "1:02",  "pm", "ORD", "07/19/2022", "6:22", "pm");

    }

    //TODO add test for -print option
}