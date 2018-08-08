package com.example.kyeon.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class NewActivity extends AppCompatActivity {
    TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        Spinner spinner = (Spinner)findViewById(R.id.countPerson);
        final ArrayAdapter sAdapter = ArrayAdapter.createFromResource(this, R.array.question, android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(sAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(NewActivity.this, (CharSequence) sAdapter.getItem(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button btn_comp = (Button) findViewById(R.id.btn_comp);
        btn_comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NewActivity.this, TripPlanActivity.class);
                startActivity(i);
            }
        });
    }

    public void onclickCalendar(View v) {
        Intent intent = new Intent(this, Calendar.class);
        startActivityForResult(intent, 1);
    }
}
