package edu.pdx.cs410J.awurtz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int GET_SUM_FROM_CALCULATOR = 42;
    private ArrayAdapter<Integer> sums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Button button = findViewById(R.id.button2);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "You clicked the button!", Toast.LENGTH_LONG).show();
//            }
//        });
        sums = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        ListView listView = findViewById(R.id.sumListView);
        listView.setAdapter(this.sums);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
                int sum = MainActivity.this.sums.getItem(index);
                Toast.makeText(MainActivity.this, "Clicked on sum " + sum, Toast.LENGTH_LONG).show();

            }
        });

    }

    public void launchCalculator(View view) {
        Intent intent = new Intent(MainActivity.this, CalculatorActivity.class);
        startActivityForResult(intent, GET_SUM_FROM_CALCULATOR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GET_SUM_FROM_CALCULATOR && resultCode == RESULT_OK && data != null) {
            this.sums.add(data.getIntExtra(CalculatorActivity.EXTRA_SUM, 0));
        }
    }
}