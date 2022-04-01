package edu.pdx.cs410j.airline_android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class AirlineActivity extends AppCompatActivity {

    private static final int GET_AIRLINES = 42;
    private ArrayAdapter<Airline> airlines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airline);

        airlines = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

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

        int count = airlines.getCount();
        boolean match = false;
        for(int i = 0; i < count; i++) {
            if(airlines.getItem(i).getName().equals(airlineName)) {
                match = true;
                break;
            }
        }

        if(!match) {
            Airline airline = new Airline(airlineName);
            airlines.add(airline);
        }
        else {
            Toast.makeText(AirlineActivity.this, "You can only add an airline once.", Toast.LENGTH_LONG).show();
        }

        editText.setText(null);
    }
}