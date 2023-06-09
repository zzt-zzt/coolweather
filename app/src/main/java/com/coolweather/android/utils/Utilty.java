package com.coolweather.android.utils;

import android.text.TextUtils;
import android.util.Log;

import androidx.room.Database;

import com.coolweather.android.WeatherApplication;
import com.coolweather.android.db.Dao.CityDao;
import com.coolweather.android.db.Dao.CountyDao;
import com.coolweather.android.db.Dao.ProvinceDao;
import com.coolweather.android.db.PositionDatabase;
import com.coolweather.android.db.entity.City;
import com.coolweather.android.db.entity.County;
import com.coolweather.android.db.entity.Province;
import com.coolweather.android.gson.Weather;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Utilty {
    private  static PositionDatabase positionDatabase;
    private  static ProvinceDao  provinceDao;
    private  static CityDao cityDao;
    private  static CountyDao countyDao;

    static{
        positionDatabase=WeatherApplication.getInstance().getPositionDatabase();
        provinceDao=positionDatabase.provinceDao();
        cityDao=positionDatabase.cityDao();
        countyDao=positionDatabase.countyDao();


    }



    public static  boolean handleProvinceResponse(String reponse){

       if(!TextUtils.isEmpty(reponse)){
           try {
               JSONArray AllProvince=new JSONArray(reponse);
               Log.d("ChooseArea",AllProvince.length()+"");
               for(int i=0;i<AllProvince.length();i++){
                    JSONObject provinceObject=AllProvince.getJSONObject(i);
                     Province province=new Province();
                     province.setProvinceCode(provinceObject.getInt("id"));
                     province.setProvinceName(provinceObject.getString("name"));
                     Log.d("ChooseArea",province.getProvinceName());
                     provinceDao.insert(province);
               }
               return   true;
           } catch (JSONException e) {
               e.printStackTrace();
           }
       }
       return  false;

    }

    public static boolean handleCityResponse(String response,int ProvinceId){
           if(!TextUtils.isEmpty(response)){
               try {
                   JSONArray AllCity=new JSONArray(response);
                   for(int i=0;i<AllCity.length();i++){
                       JSONObject cityObject=AllCity.getJSONObject(i);
                       City city=new City();
                       city.setCityCode(cityObject.getInt("id"));
                       city.setCityName(cityObject.getString("name"));
                       city.setProvinceId(ProvinceId);
                        cityDao.insert(city);


                   }
                   return   true;
               } catch (JSONException e) {
                   e.printStackTrace();
               }


           }
           return  false;
    }

    public  static boolean handleCountyResponse(String response,int cityId){
           if(!TextUtils.isEmpty(response)){

               try {
                   JSONArray AllCounty=new JSONArray(response);
                   for(int i=0;i<AllCounty.length();i++){
                          JSONObject countyObject=AllCounty.getJSONObject(i);
                       County county=new County();
                       county.setCountyCode(countyObject.getInt("id"));
                       county.setCountyName(countyObject.getString("name"));
                       county.setWeatherId(countyObject.getString("weather_id"));
                       county.setCityId(cityId);

                       countyDao.insert(county);

                   }
                   return   true;

               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
           return  false;
    }

    public   static Weather handleWeatherResponse(String  response){

        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather");
            String  weatherContent=jsonArray.getJSONObject(0).toString();
            return  new Gson().fromJson(weatherContent,Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  null;
    }


    public  static String  handleImgUrl(String response){
        try {
            JSONObject  jsonObject=new JSONObject(response);
            return  jsonObject.getString("imgurl");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  null;
    }

}
