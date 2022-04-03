package edu.pdx.cs410j.airline_android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;

public class SearchActivity extends AppCompatActivity {

    public static final String AIRLINE_ARRAY = "AIRLINE_ARRAY";
    private final ArrayList<Airline> airlineArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();

        if (intent.hasExtra(AIRLINE_ARRAY)) {
            airlineArrayList.addAll(intent.getParcelableArrayListExtra(AIRLINE_ARRAY));
        }
    }


    public void searchFlights(View view) {
        String airlineName, sourceAirport, destinationAirport;
        boolean goodData = false;

        EditText editText = findViewById(R.id.editTextAirlineName);
        airlineName = editText.getText().toString();

        editText = findViewById(R.id.editTextSource);
        sourceAirport = editText.getText().toString();

        editText = findViewById(R.id.editTextDestination);
        destinationAirport = editText.getText().toString();

        if(airlineName.equals("")) {
            Toast.makeText(SearchActivity.this, "You must specify and airline.",
                    Toast.LENGTH_LONG).show();
        } else if (sourceAirport.equals("")) {
            Toast.makeText(SearchActivity.this,
                    "Enter a valid 3 letter airport code for source.", Toast.LENGTH_LONG).show();
        } else if (destinationAirport.equals("")) {
            Toast.makeText(SearchActivity.this,
                    "Enter a valid 3 letter airport code for destination.", Toast.LENGTH_LONG).show();
        } else {
            try {
                sourceAirport = Flight.parseAirportCode(sourceAirport);
                destinationAirport = Flight.parseAirportCode(destinationAirport);
                goodData = true;
            } catch (InvalidAirportCodeException ex) {
                Toast.makeText(SearchActivity.this, ex.getInvalidAirportCode() +
                        " is not a valid airport code.", Toast.LENGTH_LONG).show();
            }
        }

        if(goodData) {

            boolean airlineMatch = false;
            Airline resultAirline = null;

            for (Airline airline : airlineArrayList) {
                if (airlineMatch) {
                    break;
                }

                if (airline.getName().equals(airlineName)) {

                    airlineMatch = true;
                    resultAirline = new Airline(airlineName);

                    Collection<Flight> flights = airline.getFlights();
                    for (Flight flight : flights) {
                        if (flight.getSource().equals(sourceAirport) &&
                                flight.getDestination().equals(destinationAirport)) {
                            resultAirline.addFlight(flight);
                        }
                    }
                }
            }

            launchSearchResults(resultAirline);
        }

    }

    private void launchSearchResults(Airline resultAirline) {
        Intent intent = new Intent(SearchActivity.this, SearchResultsActivity.class);
        intent.putExtra(SearchResultsActivity.SEARCH_RESULTS, (Parcelable) resultAirline);
        startActivity(intent);
    }
}