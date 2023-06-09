package com.coolweather.android;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.coolweather.android.db.PositionDatabase;

public class WeatherApplication  extends Application {
     public   static Context mContext;
     private  static WeatherApplication mApplication;
     private PositionDatabase positionDatabase;


     public  static WeatherApplication  getInstance(){
            return  mApplication;
     }
     @Override
     public void onCreate() {
          super.onCreate();
          mContext=getApplicationContext();
          mApplication=this;
          //构建地理位置数据库的实例
          positionDatabase= Room.databaseBuilder(this,PositionDatabase.class,"Position")
                  .addMigrations()    //允许迁移数据库
                  .allowMainThreadQueries()   //允许主线程操作数据库
                  .build();
     }

     public PositionDatabase getPositionDatabase(){
          return   positionDatabase;
     }
}
