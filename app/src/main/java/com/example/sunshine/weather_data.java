package com.example.sunshine;

import org.json.JSONException;
import org.json.JSONObject;

public class weather_data {
    private String temprature1;
    private String temprature2;
    private String icon;
    private String city;
    private String weather_type;
    private String humidity;
    private int conditions;   //for weather id

    public static weather_data fromJson(JSONObject jsonObject)
    {
        try
        {
            weather_data weatherD = new weather_data();
            weatherD.city= jsonObject.getString("name");
            //we are getting the weather id here
            weatherD.conditions=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            //with the help of weather id we will find icon and weather type
            weatherD.weather_type=jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            //weathericon will be updated using the conditions(weather id)
            weatherD.icon= updateWeatherIcon(weatherD.conditions);
            //getting temprature as a double and rounding off the value so obtained
            //then storing the value as string
            double temp1=jsonObject.getJSONObject("main").getDouble("temp")-273.15;
            int roundedValue = (int)Math.rint(temp1);
            weatherD.temprature1=Integer.toString(roundedValue);
            double temp2= jsonObject.getJSONObject("main").getDouble("feels_like")-273.15;
            int roundedvalue2 = (int)Math.rint(temp2);
            weatherD.temprature2=Integer.toString(roundedvalue2);
            weatherD.humidity=jsonObject.getJSONObject("main").getString("humidity");

            return weatherD;

        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }

    }

    private static String updateWeatherIcon(int conditions)
    {
        if(conditions>=0&&conditions<300)
        {
            return "thunderstorm";
        }
        else if(conditions>=300&&conditions<500)
        {
            return "light_rain";
        }
        else if(conditions>=500&&conditions<600)
        {
            return "heavy_rain";
        }
        else if(conditions>=600&&conditions<700)
        {
            return "snowy";
        }
        else if(conditions>=700&&conditions<800)
        {
            return "foggy";
        }
        else if(conditions>=801&&conditions<804)
        {
            return "cloudy";
        }
        else if(conditions==800) {
            return "sunny";
        }
        return "finding";
    }

    //all these are declared as private
    //to use them in mainactivity.java am using getter
    public String getTemprature1() {
        return temprature1+"°C";
    }

    public String getTemprature2() {
        return temprature2+"°C";
    }

    public String getIcon() {
        return icon;
    }

    public String getCity() {
        return city;
    }

    public String getWeather_type() {
        return weather_type;
    }

    public String getHumidity() {
        return humidity;
    }
}
