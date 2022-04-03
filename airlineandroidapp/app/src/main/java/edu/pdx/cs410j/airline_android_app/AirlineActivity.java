package edu.pdx.cs410j.airline_android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class AirlineActivity extends AppCompatActivity {

    public static final String AIRLINE_ARRAY = "AIRLINE_ARRAY";
    public static final String AIRLINE = "AIRLINE";

    private ArrayAdapter<Airline> airlines;
    private final ArrayList<Airline> airlineArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airline);

        airlines = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        Intent intent = getIntent();

        if (intent.hasExtra(AIRLINE_ARRAY)) {
            airlineArrayList.addAll(intent.getParcelableArrayListExtra(AIRLINE_ARRAY));
            airlines.addAll(airlineArrayList);
        }

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(this.airlines);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
                Airline airline = AirlineActivity.this.airlines.getItem(index);
                //Toast.makeText(AirlineActivity.this, "Clicked on " + airline, Toast.LENGTH_LONG).show();
                launchDisplayAirlineAndFlightsActivity(airline);
            }
        });
    }

    private void launchDisplayAirlineAndFlightsActivity(Airline airline) {
        Intent intent = new Intent(AirlineActivity.this, DisplayAirlineAndFlightsActivity.class);
        intent.putExtra(DisplayAirlineAndFlightsActivity.AIRLINE, (Parcelable) airline);
        startActivity(intent);
    }

    public void addAirline(View view) {
        EditText editText = findViewById(R.id.editTextTextAirlineName);
        String airlineName = editText.getText().toString();
        if(airlineName.equals("")) {
            Toast.makeText(AirlineActivity.this, "Enter an airline name.", Toast.LENGTH_LONG).show();
        }
        else {
            int count = airlines.getCount();
            boolean match = false;
            for (int i = 0; i < count; i++) {
                if (airlines.getItem(i).getName().equals(airlineName)) {
                    match = true;
                    break;
                }
            }

            if (!match) {
                Airline airline = new Airline(airlineName);
                airlines.add(airline);
            } else {
                Toast.makeText(AirlineActivity.this, "You can only add an airline once.", Toast.LENGTH_LONG).show();
            }
        }
        editText.setText(null);

    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();

        int size = airlineArrayList.size();
        airlineArrayList.clear();

        for(int i = size; i < airlines.getCount(); ++i) {
            airlineArrayList.add(airlines.getItem(i));
        }

        data.putParcelableArrayListExtra(AIRLINE_ARRAY, airlineArrayList);
        setResult(RESULT_OK, data);
        super.onBackPressed();
        finish();
    }

    @Override
    public void onPause() {
        if(airlineArrayList.size() != 0) {
            writeAirlinesToDisk();
        }
        super.onPause();
    }

    private void writeAirlinesToDisk() {
        for (Airline airline :
                airlineArrayList) {

            File file = new File(this.getFilesDir(), airline.getName() + ".txt");

            try(PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                TextDumper dumper = new TextDumper(pw);
                dumper.dump(airline);

            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}