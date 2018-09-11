package com.example.kyeon.myapplication;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Score extends AsyncTask<String, Void, String> {
    String result;
    String url, placeId, category;
    String UID;
    int flag;
    public Score(int flag, String UID, String placeId, String category) {
        this.flag = flag;
        this.UID = UID;
        this.placeId = placeId;
        this.category = category;
    }


    @Override
    protected String doInBackground(String... params) {
        try {
            url = "http://35.189.138.177:8080/score?flag=" + flag + "&UID=" +UID +"&placeId=" +placeId +"&category=" + category;
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

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
