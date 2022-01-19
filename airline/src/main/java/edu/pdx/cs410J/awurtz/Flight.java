package edu.pdx.cs410J.awurtz;

import edu.pdx.cs410J.AbstractFlight;

public class Flight extends AbstractFlight {

  String source;
  Integer flightNumber;
  String departDate;
  String departTime;
  String destination;
  String arriveDate;
  String arriveTime;
  /**
   * Creates a new <code>Flight</code> with default parameters
   */
  public Flight() {
  }
  /**
   * Creates  a new <code>Flight</code>
   *
   * @param flightNumber
   *        The flight number
   * @param src
   *        Three-letter code of the departure airport
   * @param departDate
   *        Departure date
   * @param departTime
   *        Departure time (24-hour time)
   * @param dest
   *        Three-letter code of arrival airport
   * @param arriveDate
   *        Arrival date
   * @param arriveTime
   *        Arrival time (24-hour time)
   */
  public Flight(String src, Integer flightNumber, String departDate, String departTime, String dest, String arriveDate, String arriveTime) {

    this.source = src;
    this.flightNumber = flightNumber;
    this.departDate = departDate;
    this.departTime = departTime;
    this.destination = dest;
    this.arriveDate = arriveDate;
    this.arriveTime = arriveTime;
  }
  @Override
  public int getNumber() {
  return this.flightNumber;
  }

  @Override
  public String getSource() {
    return this.source;
  }

  @Override
  public String getDepartureString() {
    return this.departTime + " " + this.departDate;
  }

  @Override
  public String getDestination() {
  return this.destination;
  }

  @Override
  public String getArrivalString() {
    return this.arriveTime + " " + this.arriveDate;
  }

}

