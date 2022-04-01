package edu.pdx.cs410J.awurtz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CalculatorActivity extends AppCompatActivity {

    public static final String EXTRA_SUM = "SUM";
    int sum;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
    }

    public void calculateSum(View view) {
        EditText leftOperand = findViewById(R.id.leftOperand);
        EditText rightOperand = findViewById(R.id.rightOperand);

        String left = leftOperand.getText().toString();
        String right = rightOperand.getText().toString();

        this.sum = Integer.parseInt(left) + Integer.parseInt(right);

        TextView sumField = findViewById(R.id.sumField);
        sumField.setText(String.valueOf(this.sum));

    }

    public void returnToMain(View view) {
        Intent data = new Intent();
        data.putExtra(EXTRA_SUM, this.sum);
        setResult(RESULT_OK, data);
        finish();
    }
}