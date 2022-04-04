package edu.pdx.cs410j.airline_android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.StringWriter;
import java.util.ArrayList;

public class DisplayAirlineAndFlightsActivity extends AppCompatActivity {

    public static final String AIRLINE = "AIRLINE";
    private Airline airline = null;
    private ArrayList<Flight> flights = new ArrayList<>();
    private ArrayAdapter<String> prettyFlights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_airline_and_flights);

        Intent intent = getIntent();

        prettyFlights = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(this.prettyFlights);

        if (intent.hasExtra(AIRLINE)) {
            airline = intent.getParcelableExtra(AIRLINE);

            if(airline.getFlightCount() > 0) {
                flights.addAll(airline.getFlights());


                prettyFlights.add(airline.toString());

                for (Flight flight: flights) {
                    StringWriter sw = new StringWriter();
                    PrettyPrinter printer = new PrettyPrinter(sw);
                    printer.dump(flight);
                    prettyFlights.add(sw.toString());
                    sw.flush();
                }

            } else {
                prettyFlights.add(airline.getName() + " has 0 flights");
            }
        }

    }
}