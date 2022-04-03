package edu.pdx.cs410j.airline_android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;

public class DisplayAirlineAndFlightsActivity extends AppCompatActivity {

    public static final String AIRLINE = "AIRLINE";
    private ArrayAdapter airline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_airline_and_flights);

        Airline airline;
        Intent intent = getIntent();

        if (intent.hasExtra(AIRLINE)) {
            airline = intent.getParcelableExtra(AIRLINE);
        }

        //TODO either change to text view or add individual flights to listView
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter();
    }
}