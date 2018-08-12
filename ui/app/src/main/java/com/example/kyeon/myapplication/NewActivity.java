package com.example.kyeon.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class NewActivity extends AppCompatActivity {
    String travel_title;
    private final int REQUEST_CODE_CALENDAR = 100;
    int AorD = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        EditText eTitle = (EditText)findViewById(R.id.travelTitle);
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
                finish();
            }
        });


        travel_title = eTitle.getText().toString();

    }

    public void onclickCalendarA(View v) {
        Intent intent = new Intent(this, Calendar.class);
        startActivityForResult(intent, REQUEST_CODE_CALENDAR);
        AorD = 1;

    }

    public void onclickCalendarB(View v) {
        Intent intent = new Intent(this, Calendar.class);
        startActivityForResult(intent, REQUEST_CODE_CALENDAR);
        AorD = 2;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK)
            return;

        if(requestCode == REQUEST_CODE_CALENDAR) {
            String date = data.getStringExtra("date");
            String year = data.getStringExtra("year");
            String month = data.getStringExtra("month");
            String day = data.getStringExtra("day");

            int yy = Integer.parseInt(year);
            int mm = Integer.parseInt(month);
            int dd = Integer.parseInt(day);

            System.out.println(date);
            System.out.println(yy);
            System.out.println(mm);
            System.out.println(dd);

            if(AorD == 1) {
                Button button = (Button)findViewById(R.id.departingDate);
                button.setText(date);
            }
            if(AorD == 2) {
                Button button = (Button)findViewById(R.id.arrivingDate);
                button.setText(date);
            }
        }
    }
    @Override
    public void onBackPressed() {

        this.finish();
    }

}

