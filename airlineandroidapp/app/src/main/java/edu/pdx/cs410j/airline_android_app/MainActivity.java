package edu.pdx.cs410j.airline_android_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int GET_AIRLINES = 42;
    private ArrayList<Airline> airlines = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchAirlineActivity(View view) {
        Intent intent = new Intent(MainActivity.this, AirlineActivity.class);
        if(airlines != null) {
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
        startActivity(intent);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GET_AIRLINES && resultCode == RESULT_OK && data != null) {
            airlines.addAll(data.getParcelableArrayListExtra(AirlineActivity.AIRLINE_ARRAY));
            //TODO write airlines to disk
        }
    }
}