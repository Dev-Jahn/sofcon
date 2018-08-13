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

    int yy = 0, mm = 0, dd = 0; // yy : defines year | mm : defines month | dd : defines day
    String date, year, month, day;
    String d_yy, d_mm, d_dd;
    String a_yy, a_mm, a_dd;
    int personCount = 0; // variable for spinner selection, for each selection, personCount gets selected item's data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        final EditText eTitle = (EditText)findViewById(R.id.travelTitle);
        Spinner spinner = (Spinner)findViewById(R.id.countPerson);
        final ArrayAdapter sAdapter = ArrayAdapter.createFromResource(this, R.array.question, android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(sAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(NewActivity.this, (CharSequence) sAdapter.getItem(i), Toast.LENGTH_SHORT).show();
                /*********************************
                   Spinner index starts from 0.
                   index[0] = 선택;
                   index[1] = 2명;
                   index[2] = 3명;
                   index[3] = 4명;
                   index[4] = 단체;
                 *********************************/

                if(i == 1) {
                    personCount = 2;
                }
                if(i == 2) {
                    personCount = 3;
                }
                if(i == 3) {
                    personCount = 4;
                }
                if(i == 4) {
                    personCount = 99; // group consist of more than 4 members is defined as group. (integer number 99 means group)
                }
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

                i.putExtra("departing_year", d_yy);
                i.putExtra("departing_month", d_mm);
                i.putExtra("departing_day", d_dd);
                i.putExtra("arriving_year", a_yy);
                i.putExtra("arriving_month", a_mm);
                i.putExtra("arriving_day", a_dd);
                i.putExtra("person_count", String.valueOf(personCount));
                i.putExtra("title_text", eTitle.getText().toString());
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
            date = data.getStringExtra("date");
            year = data.getStringExtra("year");
            month = data.getStringExtra("month");
            day = data.getStringExtra("day");

            yy = Integer.parseInt(year);
            mm = Integer.parseInt(month);
            dd = Integer.parseInt(day);

            if(AorD == 1) {
                d_yy = year;
                d_mm = month;
                d_dd = day;
                Button button = (Button)findViewById(R.id.departingDate);
                button.setText(date);
            }

            if(AorD == 2) {
                a_yy = year;
                a_mm = month;
                a_dd = day;
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

