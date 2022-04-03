package edu.pdx.cs410j.airline_android_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int GET_AIRLINES = 42;
    private static final int GET_FLIGHTS = 43;
    private ArrayList<Airline> airlines = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            readAirlinesFromDisk();
        } catch (FileNotFoundException e) {
           Toast.makeText(this, "Files storing airline data were not found.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        if(airlines.size() != 0) {
            writeAirlinesToDisk();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        writeAirlinesToDisk();
        super.onStop();
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
        if(airlines.size() > 0) {
            intent.putParcelableArrayListExtra(AirlineActivity.AIRLINE_ARRAY, airlines);
        }
        startActivityForResult(intent, GET_FLIGHTS);
    }

    public void launchSearch(View view) {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        if(airlines.size() > 0) {
            intent.putParcelableArrayListExtra(SearchActivity.AIRLINE_ARRAY, airlines);
        }
        startActivity(intent);
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
        if(airlines.size() != 0) {
            writeAirlinesToDisk();
        }
    }

    private void writeAirlinesToDisk() {
        if(airlines.size() != 0) {

            for (Airline airline : airlines) {

                File file = new File(this.getFilesDir(), airline.getName() + ".txt");

                try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                    TextDumper dumper = new TextDumper(pw);
                    dumper.dump(airline);

                } catch (IOException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void readAirlinesFromDisk() throws FileNotFoundException {
        String[] files = this.fileList();

        if(files.length != 0) {

            for (String file : files) {
                FileInputStream fis = this.openFileInput(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fis);

                try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                    TextParser parser = new TextParser(reader);
                    airlines.add(parser.parse());
                } catch (IOException | ParserException ex) {
                    Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}