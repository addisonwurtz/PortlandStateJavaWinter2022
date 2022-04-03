package edu.pdx.cs410j.airline_android_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int GET_AIRLINES = 42;
    private static final int GET_FLIGHTS = 43;
    private ArrayList<Airline> airlines = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchAirlineActivity(View view) {
        Intent intent = new Intent(MainActivity.this, AirlineActivity.class);

        if(airlines.size() > 0) {
            intent.putParcelableArrayListExtra(AirlineActivity.AIRLINE_ARRAY, airlines);
        }
        startActivityForResult(intent, GET_AIRLINES);
    }

    public void launchReadMe(View view) {
        Intent intent = new Intent(MainActivity.this, ReadMeActivity.class);
        startActivity(intent);
    }


    public void launchFlightActivity(View view) {
        Intent intent = new Intent(MainActivity.this, FlightActivity.class);
        startActivityForResult(intent, GET_FLIGHTS);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GET_AIRLINES && resultCode == RESULT_OK && data != null) {
            airlines.addAll(data.getParcelableArrayListExtra(AirlineActivity.AIRLINE_ARRAY));
            //TODO write airlines to disk
        }
        else if (requestCode == GET_FLIGHTS && resultCode == RESULT_OK && data != null) {
            boolean airlineExists = false;
            ArrayList<Airline> tempArray = data.getParcelableArrayListExtra(FlightActivity.AIRLINES_WITH_NEW_FLIGHTS_ARRAY);

            for (Airline tempAirline: tempArray) {
                airlineExists = false;

               for(int i = 0; i < airlines.size(); i++) {

                   Airline mainAirline = airlines.get(i);
                   if(mainAirline.getName().equals(tempAirline.getName())) {
                       mainAirline.addFlightArray(tempAirline.getFlights());
                       airlineExists = true;
                       break;
                   }
               }
               if(!airlineExists) {
                   airlines.add(tempAirline);
               }
            }
        }
    }
}