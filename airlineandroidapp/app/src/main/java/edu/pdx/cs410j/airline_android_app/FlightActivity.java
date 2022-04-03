package edu.pdx.cs410j.airline_android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class FlightActivity extends AppCompatActivity {

    public static final String AIRLINES_WITH_NEW_FLIGHTS_ARRAY = "AIRLINES_WITH_NEW_FLIGHTS_ARRAY";
    ArrayList<Airline> airlineArrayList = new ArrayList<Airline>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight);
    }

    public void addFlight(View view) {
        //get all flight info from edit text widgets
        //need to make shared data structure to hold airlines. I guess it should live in entry activity
        int flightNumber;
        String airlineName, source, departDate, departTime, departAmPm, destination, arriveDate,
                arriveTime, arriveAmPm;
        EditText editText = null;

        ToggleButton departToggle = (ToggleButton) findViewById(R.id.departAmPm);
        ToggleButton arriveToggle = (ToggleButton) findViewById(R.id.arriveAmPm);

        try {
            editText = findViewById(R.id.airlineName);
            airlineName = editText.getText().toString();

            editText = findViewById(R.id.flightNumber);
            flightNumber = Integer.parseInt(editText.getText().toString());

            editText = findViewById(R.id.sourceAirport);
            source = editText.getText().toString();

            editText = findViewById(R.id.departDate);
            departDate = editText.getText().toString();

            editText = findViewById(R.id.departTime);
            departTime = editText.getText().toString();

            if(departToggle.isChecked()) {
                departAmPm = "pm";
            } else {
                departAmPm = "am";
            }

            editText = findViewById(R.id.destinationAirport);
            destination = editText.getText().toString();

            editText = findViewById(R.id.arriveDate);
            arriveDate = editText.getText().toString();

            editText = findViewById(R.id.arriveTime);
            arriveTime = editText.getText().toString();

            if(arriveToggle.isChecked()) {
                arriveAmPm = "pm";
            } else {
                arriveAmPm = "am";
            }


            Flight flight = new Flight(flightNumber, source, departDate, departTime, departAmPm,
                    destination, arriveDate, arriveTime, arriveAmPm);

            boolean airlineExists = false;

            for (Airline airline: airlineArrayList)
            {
                if(airline.getName().equals(airlineName)) {
                    airline.addFlight(flight);
                    airlineExists = true;
                }
            }

            if(!airlineExists) {
                Airline airline = new Airline(airlineName);
                airline.addFlight(flight);
                airlineArrayList.add(airline);
            }

            Toast.makeText(FlightActivity.this, flight.toString(), Toast.LENGTH_LONG).show();

            clearFlightForm(departToggle, arriveToggle);

        } catch (NumberFormatException ex) {
            Toast.makeText(FlightActivity.this, editText.getText().toString() +
                    " is not a valid flight number", Toast.LENGTH_LONG).show();
        } catch (InvalidAirportCodeException ex) {
            Toast.makeText(FlightActivity.this, ex.getInvalidAirportCode() +
                    " is not a valid airport code.", Toast.LENGTH_LONG).show();
        } catch (InvalidDateException ex) {
            Toast.makeText(FlightActivity.this, ex.getInvalidDate() +
                    " is not a valid date (mm/dd/yyyy)", Toast.LENGTH_LONG).show();
        }
        catch (Exception ex) {
            Toast.makeText(FlightActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();

        data.putParcelableArrayListExtra(AIRLINES_WITH_NEW_FLIGHTS_ARRAY, airlineArrayList);
        setResult(RESULT_OK, data);
        super.onBackPressed();
        finish();

    }


    private void clearFlightForm(ToggleButton departToggle, ToggleButton arriveToggle) {
        EditText editText;
        editText = findViewById(R.id.airlineName);
        editText.setText(null);

        editText = findViewById(R.id.flightNumber);
        editText.setText(null);

        editText = findViewById(R.id.sourceAirport);
        editText.setText(null);

        editText = findViewById(R.id.departDate);
        editText.setText(null);

        editText = findViewById(R.id.departTime);
        editText.setText(null);

        if(departToggle.isChecked()) {
            departToggle.toggle();
        }

        editText = findViewById(R.id.destinationAirport);
        editText.setText(null);

        editText = findViewById(R.id.arriveDate);
        editText.setText(null);

        editText = findViewById(R.id.arriveTime);
        editText.setText(null);

        if(arriveToggle.isChecked()) {
            arriveToggle.toggle();
        }
    }
}