package org.androidtown.server;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kimkyuyeop on 2018-08-19.
 */

public class Test extends AsyncTask<String, Void, String> {
    String result;
    String url, lat, lon, lim;
    float len;
    int flag;
    String category;
    String UID;

    public Test(String lat, String lon, float len, String lim, int flag, String category, String UID) {
        this.lon = lon;
        this.lat = lat;
        this.len = len;
        this.lim = lim;
        this.flag = flag;
        this.category = category;
        this.UID = UID;
    }
    public Test(String lat, String lon, float len, String lim, int flag)  {
        this.lon = lon;
        this.lat = lat;
        this.len = len;
        this.lim = lim;
        this.flag = flag;
    }


    @Override
    protected String doInBackground(String... params) {
        try {
            if(flag == 0)
                url = "http://35.189.138.177:8080/navi/findPlace?lat=" + lat + "&lon=" + lon + "&len=" + len + "&lim="+lim + "&flag=" + flag;
            else if(flag == 1)
                url = "http://35.189.138.177:8080/navi/findPlace?lat=" + lat + "&lon=" + lon + "&len=" + len + "&lim="+lim + "&flag=" + flag + "&category=" + category + "&UID=" + UID;
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