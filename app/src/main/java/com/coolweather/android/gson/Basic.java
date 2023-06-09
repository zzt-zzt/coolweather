package com.coolweather.android.gson;



import com.google.gson.annotations.SerializedName;

public class Basic {
     @SerializedName("id")
    public  String  weatherId;

     @SerializedName("city")
    public  String cityName;

     public Update update;

     public class Update{

         @SerializedName("loc")
         public String updateTime;
     }

}
