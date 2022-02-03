package edu.pdx.cs410J.awurtz;

import edu.pdx.cs410J.AbstractFlight;
import edu.pdx.cs410J.ParserException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    Locale currentLocale = Locale.US;
    DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT, currentLocale);
    DateFormat timeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT, currentLocale);
    return dateFormatter.format(depart) + " " + timeFormatter.format(depart);
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
    Locale currentLocale = Locale.US;
    DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT, currentLocale);
    DateFormat timeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT, currentLocale);
    return dateFormatter.format(arrive) + " " + timeFormatter.format(arrive);
  }

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

}

