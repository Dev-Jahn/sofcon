package com.example.kyeon.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;

public class TagActivity extends AppCompatActivity implements View.OnClickListener{
    ArrayList<String> tags;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        tags = new ArrayList<>();
        String[] strings;
        FlexboxLayout flexboxLayout = findViewById(R.id.flexbox);

        strings = getResources().getStringArray(R.array.tags);
        for (int i = 0; i < strings.length; i++) {
            TagButton button = new TagButton(getApplicationContext());
            button.setText(strings[i]);
            button.setOnClickListener(this);
            flexboxLayout.addView(button);
        }

    }
    @Override
    public void onClick(View view) {
        String tag = ((Button)view).getText().toString();
        if(tags.contains(tag))
        {
            tags.remove(tag);
            ((Button)view).setTextColor(getResources().getColor(R.color.colorTextAccent));
            view.setBackgroundResource(R.drawable.capsule);
        }
        else
        {
            tags.add(tag);
            ((Button)view).setTextColor(getResources().getColor(R.color.colorApp));
            view.setBackgroundResource(R.drawable.capsule_selected);
        }
    }
}
