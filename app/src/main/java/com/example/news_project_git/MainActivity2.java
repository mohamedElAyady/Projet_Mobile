package com.example.news_project_git;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
/*
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
*/
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import android.os.Bundle;

public class MainActivity2 extends AppCompatActivity {
    EditText etCity, etCountry;
    String city;
    TextView tvResult,humidite,esstima,desc,vent,cloud,pression,temperature,ville;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "e53301e27efa0b66d05045d91b2742d3";
    DecimalFormat df = new DecimalFormat("#.##");

    CardView home,meteo,favoris;

    ImageView imgView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        humidite = findViewById(R.id.humidite);
        esstima = findViewById(R.id.esstima);
        desc = findViewById(R.id.desc);
        vent = findViewById(R.id.vent);
        cloud = findViewById(R.id.cloud);
        pression = findViewById(R.id.pression);
        temperature = findViewById(R.id.temperature);
        ville = findViewById(R.id.ville);
        etCountry = findViewById(R.id.etCountry);
        etCity = findViewById(R.id.etCity);


        home = (CardView) findViewById(R.id.home);

        favoris = (CardView)findViewById(R.id.favoris);

        meteo =(CardView) findViewById(R.id.meteo);



        imgView =(ImageView) findViewById(R.id.imgView);

        imgView.setImageResource(R.drawable.weather);


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        favoris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this,Saved_Items.class);
                startActivity(intent);
                finish();
            }
        });




    }


    public void getWeatherDetails(View view) {


        String tempUrl = "";
        city= etCity.getText().toString().trim();
        String country = etCountry.getText().toString().trim();
        if(city.equals("")){
            System.out.println("City field can not be empty!");
        }else{
            if(!country.equals("")){
                tempUrl = url + "?q=" + city + "," + country + "&appid=" + appid;
            }else{
                tempUrl = url + "?q=" + city + "&appid=" + appid;
            }
            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String output = "";
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        String description = jsonObjectWeather.getString("description");
                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp") - 273.15;
                        double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                        float pressure = jsonObjectMain.getInt("pressure");
                        int humidity = jsonObjectMain.getInt("humidity");
                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        String wind = jsonObjectWind.getString("speed");
                        JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                        String clouds = jsonObjectClouds.getString("all");
                        JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                        String countryName = jsonObjectSys.getString("country");
                        String cityName = jsonResponse.getString("name");


                        ville.setText(cityName);
                        temperature.setText( df.format(temp) + " °C");
                        humidite.setText(humidity+ "%");
                        esstima.setText(df.format(feelsLike) + " °C");
                        desc.setText(description);
                        vent.setText( wind + "m/s");
                        cloud.setText(clouds + "%");
                        pression.setText(pressure + " hPa");



                        if (temp < 3){
                            imgView.setImageResource(R.drawable.snowflake);
                        }else if (temp > 20){
                            imgView.setImageResource(R.drawable.sunny);
                        }else{
                            imgView.setImageResource(R.drawable.cloudy);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
}