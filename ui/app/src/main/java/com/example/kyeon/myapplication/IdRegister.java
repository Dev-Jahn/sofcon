package com.example.kyeon.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;


public class IdRegister extends Activity {

    private Button register_abort_btn;
    private Button register_commit_btn;
    private EditText register_id_txt;
    private EditText register_passwd_txt;
    private EditText register_passwd_commit_txt;
    private EditText register_name_txt;
    private EditText register_phoneNO_txt;
    private TextView id_input_error;
    private TextView passwd_input_error;
    private TextView passwd_commit_error;
    private TextView name_input_error;
    private TextView phoneNO_input_error;

    String id, passwd, passwdCM, name_owner, phoneNO;
    int id_i = 0, passwd_i = 0, passwdCM_i = 0, name_i = 0, phone_i = 0; // identification option input flag
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_id_register);

        register_id_txt=(EditText)findViewById(R.id.register_id_txt);
        register_passwd_txt=(EditText)findViewById(R.id.register_passwd_txt);
        register_passwd_commit_txt=(EditText)findViewById(R.id.register_passwd_commit_txt);
        register_name_txt=(EditText)findViewById(R.id.register_name_txt);
        register_phoneNO_txt=(EditText)findViewById(R.id.register_phoneNO_txt);

        register_commit_btn=(Button)findViewById(R.id.register_commit_btn);
        register_commit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                id_input_error=(TextView)findViewById(R.id.id_input_error);
                passwd_input_error=(TextView)findViewById(R.id.passwd_input_error);
                passwd_commit_error=(TextView)findViewById(R.id.passwd_commit_error);
                name_input_error=(TextView)findViewById(R.id.name_input_error);
                phoneNO_input_error=(TextView)findViewById(R.id.phoneNO_input_error);

                id = register_id_txt.getText().toString();
                passwd = register_passwd_txt.getText().toString();
                passwdCM = register_passwd_commit_txt.getText().toString();
                name_owner = register_name_txt.getText().toString();
                phoneNO = register_phoneNO_txt.getText().toString();

                //Log.d("DEBUG-TEST", id + id.length());
                //Log.d("DEBUG-TEST", passwd + passwd.length());
                //Log.d("DEBUG-TEST", passwdCM + passwdCM.length());
                //Log.d("DEBUG-TEST", name_owner + name_owner.length());
                //Log.d("DEBUG-TEST", phoneNO + phoneNO.length());

                //---------------------id input logic------------------------//
                if(id.length() == 0) {
                    //Log.d("ERROR-msg", "no id input");
                    id_input_error.setText("* id가 입력되지 않았습니다.       ");
                }
                else if(id.length() < 6) {
                    //Log.d("ERROR-msg", "short id input");
                    id_input_error.setText("* id는 6자리 이상의 영문입니다.    ");
                }
                else if(id.length() > 6) {
                    for(int i = 0; i < id.length(); i++) {
                        if(id.charAt(i) >= 'a' && id.charAt(i) <= 'z') continue;
                        else if(id.charAt(i) >= 'A' && id.charAt(i) <= 'Z') continue;
                        else {
                            //Log.d("ERROR-msg", "id type not identified");
                            id_input_error.setText("* id는 6자리 이상의 영문입니다.        ");
                            break;
                        }
                    }
                    id_i = 1;
                }
                else {
                    id_input_error.setText(null);
                    id_i = 1;
                }
                /////////////////////////////////////////////////////////////////

                //-----------------------passwd input logic----------------------------//
                if(passwd.length() == 0) {
                    //Log.d("ERROR-msg", "no input passwd");
                    passwd_input_error.setText("* 패스워드가 입력되지 않았습니다.     ");
                }
                else if(passwd.length() < 6) {
                    //Log.d("ERROR-msg", "short passwd");
                    passwd_input_error.setText("* 패스워드는 6자리 이상의 영문/기호/숫자의 조합입니다.    ");
                }
                else passwd_i = 1;
                //////////////////////////////////////////////////////////////////////////

                //--------------------------passswd commit logic-------------------------//
                if(passwd.length() != passwdCM.length()) {
                    //Log.d("ERROR-msg", "passwd commit not matching");
                    passwd_commit_error.setText("* 패스워드와 일치하지 않습니다.     ");
                }

                else if(!passwd.equals(passwdCM)) {
                    //Log.d("ERROR-msg", "passwd not matching");
                    passwd_commit_error.setText("* 패스워드와 일치하지 않습니다.     ");
                }

                else passwdCM_i = 1;
                ///////////////////////////////////////////////////////////////////////////

                //--------------------------name input logic -----------------------------//
                if(name_owner.length() == 0) {
                    //Log.d("ERROR-msg", "no input name");
                    name_input_error.setText("* 이름이 입력되지 않았습니다.     ");
                }
                else name_i = 1;
                ////////////////////////////////////////////////////////////////////////////

                //-----------------------------Phone NO input logic----------------------------------//
                if(phoneNO.length() == 0) {
                    //Log.d("ERROR-msg", "no input phone no");
                    phoneNO_input_error.setText("* 전화번호가 입력되지 않았습니다.    ");
                }
                else if(phoneNO.length() != 11) {
                    //Log.d("ERROR-msg", "phone NO type incorrect");
                    phoneNO_input_error.setText("* 전화번호는 11자리의 숫자입니다.   ");
                }
                else if(phoneNO.contains("-")) {
                    //Log.d("ERROR-msg", "remove '-' symbol");
                    phoneNO_input_error.setText("'-' 기호를 생략하여 입력 해주세요.  ");
                }
                else if(phoneNO.length() == 11) {
                    for(int i = 0; i < phoneNO.length(); i++) {
                        if(phoneNO.charAt(i) < '0' || phoneNO.charAt(i) > '9') {
                            //Log.d("ERROR-msg", "number should be in");
                            phoneNO_input_error.setText("* 올바르지 않은 번호입니다.   ");
                            break;
                        }
                        else phone_i = 1;
                    }
                }
                else phone_i = 1;

                //Log.d("DATA-OUTPUT", id_i+" "+passwd_i+" "+passwdCM_i+" "+name_i+" "+phone_i+" "+"hello");
                if(id_i == 1 && passwd_i == 1 && passwdCM_i == 1 && name_i == 1 && phone_i == 1) {
                    //Log.d("DATA-OUTPUT", "commited");
                    String save;
                    save = id+" "+passwd;
                    Log.d("DATA_SET", save);

                    String dirPath = getFilesDir().getAbsolutePath();
                    File file = new File(dirPath);
                    if(!file.exists()) {
                        file.mkdirs();
                    }
                    File savefile = new File(dirPath+File.separator+"account_setup.txt");
                    try{
                        FileOutputStream fos = new FileOutputStream(savefile);
                        fos.write(save.getBytes());
                        fos.close();
                        //Log.d("SAVE_SUCCESS", savefile.getPath());
                    } catch (IOException e) { }

                    if(file.listFiles().length > 0) {
                        for(File f : file.listFiles()) {
                            String f_name = f.getName();
                            //Log.d("FILE_NAME", f_name);

                            String load_path = dirPath+File.separator+f_name;
                            try {
                                FileInputStream fis = new FileInputStream(load_path);
                                BufferedReader bufferdReader = new BufferedReader(new InputStreamReader(fis));
                                String content="", temp="";
                                while((temp = bufferdReader.readLine())!= null) {
                                    content += temp;
                                }
                                //Log.d("SAVED_MESSAGE", ""+content);

                            } catch(Exception e) {}
                        }
                    }

                    Sign s = new Sign(id, passwd, phoneNO, 0);
                    s.execute();
                    String test = "";
                    try {
                        test = s.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(IdRegister.this, "asdasd", Toast.LENGTH_SHORT).show();

                    finish();
                }
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
