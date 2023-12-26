package com.example.first;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
public class JokeActivity extends AppCompatActivity{
    Button button_get;
    TextView tv_fact;

    String joke = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);

        button_get = findViewById(R.id.button_get);
        tv_fact = findViewById(R.id.tv_fact);

        button_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new JokesLoader().execute();

            }
        });

    }

    // jokes loader
    private class JokesLoader extends AsyncTask<Void, Void, Void> {

        // get json from url
        @Override
        protected Void doInBackground(Void... voids) {
            String jsonString = getJson("https://api.chucknorris.io/jokes/random");

            //get jokes form the json API
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                joke = jsonObject.getString("value");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            joke = "";
            tv_fact.setText("Loading....");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //display joke on the Screen
            if (!joke.equals("")) {
                tv_fact.setText(joke);
            }

        }
    }


    // get json from URL
    private String getJson(String link) {
        String Stream = "";
        try {
            URL url = new URL(link);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                StringBuilder str_build = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    str_build.append(line);
                    str_build.append("\n");
                }
                Stream = str_build.toString();
                urlConnection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Stream;
    }
}
