package edu.pdx.cs410j.airline_android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ReadMeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_me);
        final TextView readMeTextView = findViewById(R.id.listView);

//        InputStream readme = ReadMeActivity.class.getResourceAsStream("readme.txt");
//        StringBuilder stringBuilder = new StringBuilder();
//
//        try (
//                BufferedReader reader = new BufferedReader(new InputStreamReader(readme))
//        ) {
//            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
//
//                stringBuilder.append(line);
//            }
//        } catch (IOException e) {
//            Toast.makeText(ReadMeActivity.this, e.toString(), Toast.LENGTH_LONG).show();
//        }
        readMeTextView.setText(R.string.read_me);
    }
}