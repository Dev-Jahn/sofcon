package com.example.kyeon.myapplication;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kimkyuyeop on 2018-08-19.
 */

public class Sign extends AsyncTask<String, Void, String> {
    String result;
    String UID, pwd, phonenum;
    int type;

    public Sign(String UID, String pwd, String phonenum, int type) {
        this.UID = UID;
        this.pwd = pwd;
        this.phonenum = phonenum;
        this.type = type;
    }
    public Sign(String UID, String pwd, int type) {
        this.UID = UID;
        this.pwd = pwd;
        this.type = type;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String url;
            String U;
            if(type == 0) {
                U = "Up";
                url = "http://35.189.138.177:8080/Sign/" + U + "?UID=" + UID+"&pwd=" + pwd + "&pnum=" + phonenum;
                URL obj = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                int retCode = conn.getResponseCode();

                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                br.close();

                result =response.toString();
                return result;
            }
            else {
                U = "In";
                url = "http://35.189.138.177:8080/Sign/" + U + "?UID=" + UID+"&pwd=" + pwd;
                URL obj = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                int retCode = conn.getResponseCode();

                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                br.close();

                result =response.toString();
                return result;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}