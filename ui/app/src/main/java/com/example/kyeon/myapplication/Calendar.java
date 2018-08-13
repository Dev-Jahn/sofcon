package com.example.kyeon.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.kyeon.myapplication.decorator.OneDayDecorator;
import com.example.kyeon.myapplication.decorator.SaturdayDecorator;
import com.example.kyeon.myapplication.decorator.SundayDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

//import com.example.kyeon.myapplication.decorator.EventDecorator;


public class Calendar extends Activity {

    TextView txtText;
    int yy = 0, mm = 0, dd = 0;
    String date;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    private final int REQUEST_CODE_ALPHA = 100;
    MaterialCalendarView materialCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_calendar);

        Button applyButton = (Button)findViewById(R.id.apply);
        materialCalendarView = (MaterialCalendarView)findViewById(R.id.calendarView);

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(java.util.Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1)) // 달력의 시작
                .setMaximumDate(CalendarDay.from(2030, 11, 31)) // 달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator());

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                yy = materialCalendarView.getSelectedDate().getYear();
                mm = materialCalendarView.getSelectedDate().getMonth()+1;
                dd = materialCalendarView.getSelectedDate().getDay();
            }
        });

        applyButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                date = yy + "/" + mm + "/" + dd;
                String year = String.valueOf(yy);
                String month = String.valueOf(mm);
                String day = String.valueOf(dd);

                Intent intent = new Intent();
                intent.putExtra("date", date);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("day", day);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    public void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}
