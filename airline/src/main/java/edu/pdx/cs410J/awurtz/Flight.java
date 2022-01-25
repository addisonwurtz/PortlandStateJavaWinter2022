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
   * @param source
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
  public Flight(Integer flightNumber, String source, String departDate, String departTime, String dest, String arriveDate, String arriveTime) {

    this.flightNumber = flightNumber;
    this.source = source;
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

  public String getDepartDate() {
    return this.departDate;
  }

  public String getDepartTime() {
    return this.departTime;
  }

  @Override
  public String getDepartureString() {
    return this.departTime + " " + this.departDate;
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
  public String getArrivalString() {
    return this.arriveTime + " " + this.arriveDate;
  }

}

