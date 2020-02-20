package com.example.programmeerproject;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;

public class RequestTask extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... uri) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response;
        String str_response = null;

        try{
            response = httpClient.execute(new HttpGet(uri[0]));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode()== HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                str_response = out.toString();
                out.close();

                // Do another request, now specifically for the venue
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str_response;
    }

    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
        Log.d("result", result);
    }
}
