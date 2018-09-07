package com.example.kyeon.myapplication;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class IdRegister extends Activity {

    private Button register_abort_btn;
    private Button register_commit_btn;
    private EditText register_id_txt;
    private EditText register_passwd_txt;
    private EditText register_passwd_commit_txt;
    private EditText register_name_txt;
    private EditText register_phoneNO_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_id_register);

        register_commit_btn=(Button)findViewById(R.id.register_commit_btn);
        register_commit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register_id_txt=findViewById(R.id.register_id_txt);
                register_passwd_txt=findViewById(R.id.register_passwd_txt);
                register_passwd_commit_txt=findViewById(R.id.register_passwd_commit_txt);
                register_name_txt=findViewById(R.id.register_name_txt);
                register_phoneNO_txt=findViewById(R.id.register_phoneNO_txt);

                System.out.println(register_id_txt.getText().toString());
                System.out.println(register_passwd_txt.getText().toString());
                System.out.println(register_passwd_commit_txt.getText().toString());
                System.out.println(register_name_txt.getText().toString());
                System.out.println(register_phoneNO_txt.getText().toString());

                finish();
            }
        });

        register_abort_btn=(Button)findViewById(R.id.register_abort_btn);
        register_abort_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
