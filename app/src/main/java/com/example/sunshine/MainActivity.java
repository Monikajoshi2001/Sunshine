package com.example.sunshine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    final String API_Id = BuildConfig.API_KEY;
    final String Weather_URL = "https://home.openweathermap.org/data/2.5/weather";
    final long min_time = 5000;   //5 sec
    final float min_distance = 1000;  //1 km
    final int request_code = 101;

    String Location_Provider = LocationManager.GPS_PROVIDER;
    LocationManager locationManager;


    TextView Temprature, weather, city;
    ImageView weatherIcon;
    Button City_finder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Temprature = findViewById(R.id.temprature);
        weather = findViewById(R.id.weather_condition);
        city = findViewById(R.id.city_name);
        weatherIcon = findViewById(R.id.weather_icon);
        City_finder = findViewById(R.id.find);

        City_finder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, city_finder.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWeatherForCurrentLocation();
    }



    private void getWeatherForCurrentLocation() {
        LocationListener locationListener = new LocationListener() {
            //when user move from one location to another
            //we will fetch the weather of that location using latitude and longitude
            @Override
            public void onLocationChanged(@NonNull Location location) {
                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());

                //fetching the weather data from website using API
                RequestParams params = new RequestParams();
                params.put("lat",Latitude);
                params.put("long",Longitude);
                params.put("APIid",API_Id);
                letsDoSomeNetworking(params);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            //when user allow the access to the location
            @Override
            public void onProviderEnabled(String provider) {

            }

            //when user allow the access to the location
            //but we are not able to fetch the location
            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(MainActivity.this, "Not able to fetch the location", Toast.LENGTH_LONG).show();
            }
        };



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(Location_Provider, min_time, min_distance, locationListener);
    }

    //checking if user allows the location or not
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==request_code)
        {
            //if the location access is allowed we will call getWeatherForCurrentLocation()
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(MainActivity.this,"Location accessed",Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLocation();
            }
            //if location access is not allowed
            else
            {
                Toast.makeText(MainActivity.this,"Location access is denied",Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void letsDoSomeNetworking(RequestParams params)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Weather_URL,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(MainActivity.this,"Data recieved successfully",Toast.LENGTH_SHORT).show();
               // super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
               // super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }




}
