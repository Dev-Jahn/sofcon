package com.example.tjsan.ui;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btn_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_login = (Button) findViewById(R.id.login_btn);

        btn_login.setOnClickListener(LoginClickListener);
    }
    Button.OnClickListener LoginClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {
            String id;
            String pw;
            id = ((TextInputEditText) findViewById(R.id.id_text)).getText().toString();//use this as a view
            pw = ((TextInputEditText) findViewById(R.id.pw_text)).getText().toString();
        }
    };
}
