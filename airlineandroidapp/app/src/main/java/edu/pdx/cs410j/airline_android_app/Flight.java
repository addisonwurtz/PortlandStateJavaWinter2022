package edu.pdx.cs410j.airline_android_app;

import edu.pdx.cs410J.AbstractFlight;
import edu.pdx.cs410J.AirportNames;
import edu.pdx.cs410J.ParserException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Integer.parseInt;
import static java.text.DateFormat.Field.*;
import static java.time.temporal.ChronoField.YEAR;

/**
 * code for <code>Flight</code> class
 */
public class Flight extends AbstractFlight implements Comparable<Flight>{

    String source;
    Integer flightNumber;
    String departDate;
    String departTime;
    Date depart;
    String destination;
    String arriveDate;
    String arriveTime;
    Date arrive;
    static final String dateTimePattern = "MM/dd/yyyy hh:mm a";
    /**
     * Creates a new <code>Flight</code> with default parameters
     */
    public Flight() {}

    /**
     * Creates  a new <code>Flight</code>
     *
     * @param flightNumber
     *        The flight number
     * @param source
     *        Three-letter code of the departure airport
     * @param departDate
     *        Departure date
     * @param departTime
     *        Departure time (am/pm)
     * @param dest
     *        Three-letter code of arrival airport
     * @param arriveDate
     *        Arrival date
     * @param arriveTime
     *        Arrival time (am/pm)
     */
    public Flight(Integer flightNumber, String source, String departDate, String departTime, String dest,
                  String arriveDate, String arriveTime) {

        this.flightNumber = flightNumber;
        this.source = source;
        this.departDate = departDate;
        this.departTime = departTime;
        this.depart = getDeparture();
        this.destination = dest;
        this.arriveDate = arriveDate;
        this.arriveTime = arriveTime;
        this.arrive = getArrival();

        if(depart.after(arrive)) {
            throw new InvalidTimeException("Departure time " + depart + " is after arrival time " + arrive + " Departure" +
                    "times must before arrival times.");
        }
    }

    /**
     * Constructor builds flight from command line arguments
     * returns a flight built from the freshly parsed parameters.
     * @param args commandline arguments (options are ignored in this method)
     * @throws MissingCommandLineArgumentException that specifies which argument is missing
     */
    public Flight(String[] args) {
        String[] flightArgs;
        int i = 0, j = 0;

        while(args[i].contains("-")) {
            if(args[i].equals("-host")) { ++i; }
            if(args[i].equals("-port")) { ++i; }
            if(args[i].equals("-search")) { ++i; }
            ++i;
        }
        flightArgs = Arrays.copyOfRange(args, ++i, args.length);

        try {
            this.flightNumber = parseInt(flightArgs[j]);
        } catch(NumberFormatException ex) {
            throw new InvalidFlightNumberException(flightArgs[j]);
        } catch(ArrayIndexOutOfBoundsException ex) {
            throw new MissingCommandLineArgumentException("flight number");
        }
        try {
            this.source = parseAirportCode(flightArgs[++j]);
        } catch(ArrayIndexOutOfBoundsException ex) {
            throw new MissingCommandLineArgumentException("source airport code");
        }
        try {
            this.departDate = parseDate(flightArgs[++j]);
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new MissingCommandLineArgumentException("departure date");
        }
        try {
            this.departTime = parseTime(flightArgs[++j], flightArgs[++j]);
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new MissingCommandLineArgumentException("departure time");
        }
        this.depart = getDeparture();
        try {
            this.destination = parseAirportCode(flightArgs[++j]);
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new MissingCommandLineArgumentException("destination airport");
        }
        try {
            this.arriveDate = parseDate(flightArgs[++j]);
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new MissingCommandLineArgumentException("arrival date");
        }
        try {
            this.arriveTime = parseTime(flightArgs[++j], flightArgs[++j]);
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new MissingCommandLineArgumentException("arrival time");
        }
        this.arrive = getArrival();

        if(depart.after(arrive)) {
            throw new InvalidTimeException("Departure time " + depart + " is after arrival time " + arrive + " Departure" +
                    "times must before arrival times.");
        }
    }

    /**
     * Constructs flight from DOM element
     * @param root representing flight
     */
    public Flight(Element root) {
        NodeList nodeList = root.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (!(node instanceof Element)) {
                continue;
            }
            Element element = (Element) node;
            switch (element.getNodeName()) {
                case "number" -> this.flightNumber = Integer.valueOf(element.getTextContent());
                case "src" -> this.source = element.getTextContent();
                case "depart" -> {
                    GregorianCalendar calendar;
                    int day = 0, month = 0, year = 0, hour = 0, minute = 0;

                    NodeList dateNodes = element.getChildNodes();
                    for (int j = 0; j < dateNodes.getLength(); j++) {
                        Node dateNode = dateNodes.item(j);
                        if (!(dateNode instanceof Element)) {
                            continue;
                        }
                        Element dateElement = (Element) dateNode;
                        switch (dateElement.getNodeName()) {
                            case "date" -> {
                                day = Integer.parseInt(dateElement.getAttribute("day"));
                                month = Integer.parseInt(dateElement.getAttribute("month"));
                                year = Integer.parseInt(dateElement.getAttribute("year"));
                            }
                            case "time" -> {
                                hour = Integer.parseInt(dateElement.getAttribute("hour"));
                                minute = Integer.parseInt(dateElement.getAttribute("minute"));
                            }
                        }
                    }
                    calendar = new GregorianCalendar(year, month, day, hour, minute);
                    this.depart = calendar.getTime();
                    this.departDate = month + "/" + day + "/" + year;
                    if (hour > 12) {
                        this.departTime = (hour - 12) + ":" + minute + " pm";
                    } else {
                        this.departTime = hour + ":" + minute + " am";
                    }
                }
                case "dest" -> this.destination = element.getTextContent();
                case "arrive" -> {
                    GregorianCalendar calendar;
                    int day = 0, month = 0, year = 0, hour = 0, minute = 0;

                    NodeList dateNodes = element.getChildNodes();
                    for (int j = 0; j < dateNodes.getLength(); j++) {
                        Node dateNode = dateNodes.item(j);
                        if (!(dateNode instanceof Element)) {
                            continue;
                        }
                        Element dateElement = (Element) dateNode;
                        switch (dateElement.getNodeName()) {
                            case "date" -> {
                                day = Integer.parseInt(dateElement.getAttribute("day"));
                                month = Integer.parseInt(dateElement.getAttribute("month"));
                                year = Integer.parseInt(dateElement.getAttribute("year"));
                            }
                            case "time" -> {
                                hour = Integer.parseInt(dateElement.getAttribute("hour"));
                                minute = Integer.parseInt(dateElement.getAttribute("minute"));
                            }
                        }
                    }
                    calendar = new GregorianCalendar(year, month, day, hour, minute);
                    this.arrive = calendar.getTime();
                    this.arriveDate = month + "/" + day + "/" + year;
                    if (hour > 12) {
                        this.arriveTime = (hour - 12) + ":" + minute + " pm";
                    } else {
                        this.arriveTime = hour + ":" + minute + " am";
                    }
                }
            }
        }

    }

    public Flight(int flightNumber, String source) {

        this.flightNumber = flightNumber;
        this.source = source;
        this.departDate = "03/12/2022";
        this.departTime = "6:30 am";
        this.depart = getDeparture();
        this.destination = "LAX";
        this.arriveDate = "03/12/2022";
        this.arriveTime = "10:30 am";
        this.arrive = getArrival();
    }

    public Flight(int flightNumber, String source, String departure, String destination, String arrival) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateTimePattern);

        try {
            this.flightNumber = flightNumber;
            this.source = source;
            this.depart = simpleDateFormat.parse(departure);
            this.destination = destination;
            this.arrive = simpleDateFormat.parse(arrival);

        } catch (ParseException ex) {
            System.err.println("Invalid date format: " + ex.getMessage());
            System.exit(1);
        }
    }

    @Override
    public int getNumber() {
        return this.flightNumber;
    }

    @Override
    public String getSource() {
        return this.source;
    }

    public String getDepartDate() {
        return this.departDate;
    }

    public String getDepartTime() {
        return this.departTime;
    }

    @Override
    public Date getDeparture() {
        if(depart == null) {
            SimpleDateFormat stringToDate = new SimpleDateFormat(dateTimePattern);
            String dateString = this.departDate + " " + this.departTime;

            try {
                return stringToDate.parse(dateString);
            } catch (ParseException e) {
                throw new InvalidDepartureException("Departure date/Time (" + dateString + ") is not in the format MM/dd/yyyy hh:mm am/pm");
            }
        }
        else {
            return depart;
        }
    }

    @Override
    public String getDepartureString() {
        String dateTimePattern = "MM/dd/yyyy hh:mm a";
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateTimePattern);
        return dateFormat.format(this.getDeparture());
/*
        return dateFormatter.format(depart) + " " + timeFormatter.format(depart);
        Locale currentLocale = Locale.US;
        DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT, currentLocale);
        DateFormat timeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT, currentLocale);

 */
    }

    @Override
    public String getDestination() {
        return this.destination;
    }

    public String getArriveDate() {
        return this.arriveDate;
    }

    public String getArriveTime() {
        return this.arriveTime;
    }

    @Override
    public Date getArrival() {
        if(arrive == null) {
            SimpleDateFormat stringToDate = new SimpleDateFormat(dateTimePattern);
            String dateString = this.arriveDate + " " + this.arriveTime;

            try {
                return stringToDate.parse(dateString);
            } catch (ParseException e) {
                throw new InvalidArrivalException("Arrival date/Time (" + dateString + ") is not in the format MM/dd/yyyy hh:mm");
            }
        }
        else {
            return arrive;
        }
    }

    @Override
    public String getArrivalString() {
        String dateTimePattern = "MM/dd/yyyy hh:mm a";
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateTimePattern);
        return dateFormat.format(this.getArrival());
        /*
        Locale currentLocale = Locale.US;
        DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT, currentLocale);
        DateFormat timeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT, currentLocale);
        return dateFormatter.format(arrive) + " " + timeFormatter.format(arrive);

         */
    }

    /*
    Flights are sorted alphabetically by the source airport code, and if they both have the same source airport, they
    are sorted by departure time.
     */
    @Override
    public int compareTo(Flight flight) {
        int sourceCmp = this.source.compareTo(flight.getSource());
        int departureCmp;

        if(sourceCmp == 0 ) {
            departureCmp = this.getDeparture().compareTo(flight.getDeparture());
            if(departureCmp == 0) {
                return 0;
            }
            if(departureCmp < 0) {
                return -1;
            }
            else {
                return 1;
            }
        }
        if(sourceCmp > 0) {
            return 1;
        }
        else {
            return -1; }
    }

    /**
     * Traverses a string and checks if each character in the string is a letter.
     * @param string is the 3 letter airport code string
     * @return valid 3-letter airport code.
     */
    static String parseAirportCode(String string) {
        if (string == null || string.length() != 3) {
            throw new InvalidAirportCodeException(string);
        }

        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (!(c >= 'A' && c <= 'Z') && !(c >= 'a' && c <= 'z')) {
                throw new InvalidAirportCodeException(string);
            }
        }
        if(AirportNames.getName(string.toUpperCase()) == null) {
            throw new InvalidAirportCodeException(string);
        }
        return string.toUpperCase();
    }


    /**
     * The parseDate method parses dates, verifying the format is mm/dd/yyyy or m/d/yyy (months and days can include or
     * omit the leading zero).
     * If the date is properly formatted, the date string is returned. If it is a not exception with a descriptive error
     * message is thrown.
     * @param dateString to be parsed (that is hopefully a properly formatted date)
     * @return dateString if it is a valid date in a valid format
     * @throws InvalidDateException for dates that do not exist and dates that are not properly formatted.
     */
    static String parseDate(String dateString) throws InvalidDateException{

        StringTokenizer stringTokenizer = new StringTokenizer(dateString, "/");

        if(stringTokenizer.countTokens() != 3) {
            throw new InvalidDateException(dateString);
        }

        int month, day, year;
        try {
            month = Integer.parseInt(stringTokenizer.nextToken());
            day = Integer.parseInt(stringTokenizer.nextToken());
            year = Integer.parseInt(stringTokenizer.nextToken());
        } catch (NumberFormatException ex) {
            throw new InvalidDateException(dateString);
        }
        if(month < 1 || month > 12 || day < 1 || day > 31 || year < 1000 || year > 9999) {
            throw new InvalidDateException(dateString);
        }
        if((month == 2 && day > 29) || ((month == 4 || month == 6 || month == 9 || month == 11) && (day > 30))) {
            throw new InvalidDateException(dateString);
        }
        return dateString;
    }

    /**
     * The parseTime method takes a string and returns it if it is valid a valid time. If it is not, an error is thrown.
     * @param timeString in 12-hour time
     * @param amPm string specifying "am" or "pm"
     * @return time string if is a properly formatted, valid 24-hour time.
     */
    static String parseTime(String timeString, String amPm) {
        StringTokenizer stringTokenizer = new StringTokenizer(timeString, ":");

        if(stringTokenizer.countTokens() != 2) {
            throw new InvalidTimeException(timeString + " is not a valid time. Times should be of the form hh:mm.");
        }

        int hour, minute;
        try{
            hour = Integer.parseInt(stringTokenizer.nextToken());
            minute = Integer.parseInt(stringTokenizer.nextToken());
        } catch (NumberFormatException ex) {
            throw new InvalidTimeException(timeString + " is not a valid time.");
        }
        if((hour < 1 || hour > 12) || (minute < 0 || minute > 60)) {
            throw new InvalidTimeException(timeString + " is not a valid 12-hour time.");
        }
        if(amPm.equalsIgnoreCase("am") || amPm.equalsIgnoreCase("pm")) {
            return timeString + " " + amPm;
        }
        else {
            throw new InvalidTimeException("The time " + timeString + " must be followed by \"am\" or \"pm\". You entered "
                    + amPm);
        }
    }

    static String parseDateTime(String dateTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateTimePattern);
        Date date;
        try {
            date = dateFormat.parse(dateTime);
        } catch (ParseException ex) {
            throw new InvalidDateException(dateTime);
        }

        String dateString, timeString, monthString, dayString, yearString;

        StringTokenizer stringTokenizer = new StringTokenizer(dateTime, " ");

        if(stringTokenizer.countTokens() != 3) {
            throw new InvalidDateException(dateTime);
        }
        else {
            try {
                dateString = stringTokenizer.nextToken();
                StringTokenizer dateTokenizer = new StringTokenizer(dateString, "/");
                monthString = dateTokenizer.nextToken();
                dayString = dateTokenizer.nextToken();
                yearString = dateTokenizer.nextToken();
                if(parseInt(monthString) >  12 || parseInt(dayString) > 31) {
                    throw new InvalidDateException(dateTime);
                }
                int length = yearString.length();
                if(!(length == 2 || length == 4)) {
                    throw new InvalidDateException(dateTime);
                }

                timeString = parseTime(stringTokenizer.nextToken(), stringTokenizer.nextToken());

            } catch (InvalidDateException | InvalidTimeException | NumberFormatException ex) {
                throw new InvalidDateException(dateTime);
            }
        }


        return dateFormat.format(date);
    }
}

