package edu.pdx.cs410j.airline_android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AirlineActivity extends AppCompatActivity {

    public static final String AIRLINE_ARRAY = "AIRLINE_ARRAY";
    public static final String AIRLINE = "AIRLINE";

    private ArrayAdapter<Airline> airlines;
    private ArrayList<Airline> airlineArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airline);

        airlines = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        Intent intent = getIntent();

        if(intent.hasExtra(AIRLINE)) {
            airlines.add(intent.getParcelableExtra(AIRLINE));
        }

        if (intent.hasExtra(AIRLINE_ARRAY)) {
            airlineArrayList.addAll(intent.getParcelableArrayListExtra(AIRLINE_ARRAY));
            airlines.addAll(airlineArrayList);
//            for (Airline airline : airlineArrayList) {
//                airlines.add(airline);
//            }
        }

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(this.airlines);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
                Airline airline = AirlineActivity.this.airlines.getItem(index);
                Toast.makeText(AirlineActivity.this, "Clicked on " + airline, Toast.LENGTH_LONG).show();
            }
        });
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
}