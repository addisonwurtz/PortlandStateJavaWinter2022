package edu.pdx.cs410J.awurtz;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class MessagesTest {
    @Test
    void testMissingRequiredParameterReturnsHelpfulError() {
        String parameter = "airline name";
        assertThat(Messages.missingRequiredParameter(parameter),
                containsString("The required parameter \"airline name\" is missing"));
    }
}
