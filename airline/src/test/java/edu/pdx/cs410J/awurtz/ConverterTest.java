package edu.pdx.cs410J.awurtz;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class ConverterTest extends InvokeMainTestCase{

    private InvokeMainTestCase.MainMethodResult invokeMain(String... args) {
        return invokeMain(Converter.class, args);
    }
    @Test
    void ConverterConstructorAddsFilesNames() {
       String textFile = "AirlineTestFile";
       String xmlFile = "ConverterXmlFile";

       Converter converter = new Converter(textFile, xmlFile);

       assertThat(converter.textFile, containsString(textFile));
        assertThat(converter.xmlFile, containsString(xmlFile));
    }

    @Test
    void ConvertsValidTextFileToValidAirlineXML() {
        String[] args = new String[] {"AirlineTestFile", "ConverterXmlFile"};

        MainMethodResult result = invokeMain(args);

        assertThat(result.getExitCode(), equalTo(0));
    }
}
