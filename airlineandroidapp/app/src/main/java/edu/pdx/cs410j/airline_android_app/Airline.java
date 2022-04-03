package edu.pdx.cs410j.airline_android_app;

import android.os.Parcel;
import android.os.Parcelable;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.AbstractFlight;

/**
 * Code for Airline class.
 */
public class Airline extends AbstractAirline implements Parcelable {
    /** Name of airline */
    private final String name;
    /** List of airline's flights */
    private ArrayList<Flight> flights = new ArrayList<>();

    /**
     * Airline Constructor
     * @param name of the airline
     */
    public Airline(String name) {
        this.name = name;
    }


    protected Airline(Parcel in) {
        name = in.readString();
        flights = in.createTypedArrayList(Flight.CREATOR);
    }

    public static final Creator<Airline> CREATOR = new Creator<Airline>() {
        @Override
        public Airline createFromParcel(Parcel in) {
            return new Airline(in);
        }

        @Override
        public Airline[] newArray(int size) {
            return new Airline[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeTypedList(flights);
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void addFlight(AbstractFlight flight) {
        flights.add((Flight) flight);
        Collections.sort(flights);
    }

    //TODO I think this might be a problem later...
    public void addFlight(Flight flight) {
        flights.add(flight);
        Collections.sort(flights);
    }

    public void addFlightArray(Collection<Flight> newFlights){
        this.flights.addAll(newFlights);
    }

    @Override
    public Collection<Flight> getFlights() {
        Collections.sort(flights);
        return flights;
    }


}
