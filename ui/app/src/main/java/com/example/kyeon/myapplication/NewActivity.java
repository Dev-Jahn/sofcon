package com.example.kyeon.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewActivity extends AppCompatActivity {
    String travel_title;
    String place_text;

    public static final String TAG = "Alert_Dialog";
    AlertDialog.Builder alertDialogCalendar;
    AlertDialog.Builder personEmpty;
    AlertDialog.Builder departingEmpty;
    AlertDialog.Builder arrivingEmpty;
    AlertDialog.Builder titleEmpty;
    AlertDialog.Builder placeEmpty;

    final int REQUEST_CODE_CALENDAR = 100;
    int AorD = 0;
    int yy = 0, mm = 0, dd = 0; // yy : defines year | mm : defines month | dd : defines day
    String today_yy, today_mm, today_dd;
    String date, year, month, day;
    String d_yy, d_mm, d_dd;
    String a_yy, a_mm, a_dd;
    int personCount = 0; // variable for spinner selection, for each selection, personCount gets selected item's data

    int check_dyy = 0, check_dmm = 0, check_ddd = 0;
    int check_ayy = 0, check_amm = 0, check_add = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        Toolbar newToolbar;

        final EditText eTitle = (EditText) findViewById(R.id.travelTitle);
        final EditText ePlace = (EditText) findViewById(R.id.placeName);
        Spinner spinner = (Spinner) findViewById(R.id.countPerson);
        final ArrayAdapter sAdapter = ArrayAdapter.createFromResource(this, R.array.question, android.R.layout.simple_spinner_dropdown_item);
        Button departButton, arrivingButton;

        departButton = (Button)findViewById(R.id.departingDate);
        arrivingButton = (Button)findViewById(R.id.arrivingDate);

        long todayTime = System.currentTimeMillis();
        Date todayDate = new Date(todayTime);
        SimpleDateFormat todayDateFormat = new SimpleDateFormat("/yyyy/MM/dd", Locale.KOREA);
        String CurrentDate = todayDateFormat.format(todayDate);

        today_yy = CurrentDate.substring(1, 5);
        today_mm = CurrentDate.substring(6, 8);
        today_dd = CurrentDate.substring(9, 11);

        departButton.setText(today_yy+"/"+today_mm+"/"+today_dd);
        arrivingButton.setText(today_yy+"/"+today_mm+"/"+today_dd);


        newToolbar = (Toolbar)findViewById(R.id.newActivitytoolbar);
        newToolbar.setTitle("새 여행");
        setSupportActionBar(newToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.outline_list_black_18dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

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

                if(i == 0) {
                    personCount = 1;
                }

                if (i == 1) {
                    personCount = 2;
                }
                if (i == 2) {
                    personCount = 3;
                }
                if (i == 3) {
                    personCount = 4;
                }
                if (i == 4) {
                    personCount = 99; // group consist of more than 4 members is defined as group. (integer number 99 means group)
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        travel_title = eTitle.getText().toString();
        place_text = ePlace.getText().toString();

        Button btn_comp = (Button) findViewById(R.id.btn_comp);
        btn_comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NewActivity.this, TripPlanActivity.class);

                if(d_yy == null && d_mm == null && d_dd == null && a_yy == null && a_mm == null && a_dd == null) {
                    d_yy = a_yy = today_yy;
                    d_mm = a_mm = today_mm;
                    d_dd = a_dd = today_dd;
                }

                if(travel_title.length() == 0)
                    travel_title = "여행을 떠나요~!!";
                if(place_text.length() == 0)
                    place_text = "서울";
                
                i.putExtra("departing_year", d_yy);
                i.putExtra("departing_month", d_mm);
                i.putExtra("departing_day", d_dd);
                i.putExtra("arriving_year", a_yy);
                i.putExtra("arriving_month", a_mm);
                i.putExtra("arriving_day", a_dd);
                i.putExtra("person_count", String.valueOf(personCount));
                i.putExtra("title_text", travel_title);
                i.putExtra("place_name", place_text);

                startActivity(i);
                finish();
            }
        });



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

        if (resultCode != RESULT_OK)
            return;

        if (requestCode == REQUEST_CODE_CALENDAR) {
            date = data.getStringExtra("date");
            year = data.getStringExtra("year");
            month = data.getStringExtra("month");
            day = data.getStringExtra("day");

            yy = Integer.parseInt(year);
            mm = Integer.parseInt(month);
            dd = Integer.parseInt(day);

            if (AorD == 1) {
                d_yy = year;
                d_mm = month;
                d_dd = day;

                check_dyy = Integer.parseInt(d_yy);
                check_dmm = Integer.parseInt(d_mm);
                check_ddd = Integer.parseInt(d_dd);

                if(check_ayy != 0 && check_amm != 0 && check_add != 0) {
                    if (check_ayy < check_dyy || check_amm < check_dmm || check_add < check_ddd) {
                        alertDialogCalendar = new AlertDialog.Builder(NewActivity.this);
                        alertDialogCalendar.setTitle("일정 선택 오류입니다!!");
                        alertDialogCalendar.setMessage("출발일정은 도착일정보다 늦을 수 없습니다.");
                        alertDialogCalendar.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                Log.v(TAG, "확인");
                                dialog.dismiss();
                            }
                        });
                        alertDialogCalendar.show();
                    } else {
                        Button button = (Button) findViewById(R.id.departingDate);
                        button.setText(date);
                    }
                }
                else {
                    Button button = (Button) findViewById(R.id.departingDate);
                    button.setText(date);
                }

            }

            if (AorD == 2) {
                a_yy = year;
                a_mm = month;
                a_dd = day;

                check_ayy = Integer.parseInt(a_yy);
                check_amm = Integer.parseInt(a_mm);
                check_add = Integer.parseInt(a_dd);

                System.out.println(check_add);
                System.out.println(check_ddd);


                if(check_dyy != 0 && check_dmm != 0 && check_ddd != 0) {
                    if ((check_ayy < check_dyy) || (check_amm < check_dmm) || (check_add < check_ddd)) {
                        System.out.println(1);
                        alertDialogCalendar = new AlertDialog.Builder(NewActivity.this);
                        alertDialogCalendar.setTitle("일정 선택 오류입니다!!");
                        alertDialogCalendar.setMessage("도착일정은 출발 일정 보다 빠를 수 없습니다.");
                        alertDialogCalendar.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                Log.v(TAG, "확인");
                                dialog.dismiss();
                            }
                        });
                        alertDialogCalendar.show();
                    } else {
                        Button button = (Button) findViewById(R.id.arrivingDate);
                        button.setText(date);
                    }
                }
                else {
                    Button button = (Button) findViewById(R.id.arrivingDate);
                    button.setText(date);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}

