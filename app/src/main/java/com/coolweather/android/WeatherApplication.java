package com.coolweather.android;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.coolweather.android.db.PositionDatabase;

public class WeatherApplication  extends Application {
     public   static Context mContext;
     private  static WeatherApplication mApplication;


     public  static WeatherApplication  getInstance(){
            return  mApplication;
     }
     @Override
     public void onCreate() {
          super.onCreate();
          mContext=getApplicationContext();
          mApplication=this;

     }


}
