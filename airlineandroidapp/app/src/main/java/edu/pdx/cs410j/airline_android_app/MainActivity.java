package edu.pdx.cs410j.airline_android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchReadMe(View view) {
        Intent intent = new Intent(MainActivity.this, ReadMeActivity.class);
        startActivity(intent);
    }
}