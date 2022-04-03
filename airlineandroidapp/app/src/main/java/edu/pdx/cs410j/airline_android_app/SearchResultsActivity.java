package edu.pdx.cs410j.airline_android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.StringWriter;

public class SearchResultsActivity extends AppCompatActivity {

    public static final String SEARCH_RESULTS = "SEARCH_RESULTS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Intent intent = getIntent();

        Airline results = null;

        if(intent.hasExtra(SEARCH_RESULTS)) {
           results = intent.getParcelableExtra(SEARCH_RESULTS);
        }

        TextView textView = findViewById(R.id.searchResultView);
        if(results == null) {
            textView.setText(R.string.no_search_matches);
        } else {
            StringWriter sw = new StringWriter();
            PrettyPrinter printer = new PrettyPrinter(sw);
            printer.dump(results);
            textView.setText(sw.toString());
        }

    }
}