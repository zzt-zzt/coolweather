package com.coolweather.android.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.coolweather.android.db.Dao.CityDao;
import com.coolweather.android.db.Dao.CountyDao;
import com.coolweather.android.db.Dao.ProvinceDao;
import com.coolweather.android.db.entity.City;
import com.coolweather.android.db.entity.County;
import com.coolweather.android.db.entity.Province;

@Database(entities = {Province.class, City.class, County.class},version = 1,exportSchema = false)
public abstract  class PositionDatabase extends RoomDatabase {

    public  static final  String DB_NAME="Position";

    public  abstract ProvinceDao provinceDao();
    public  abstract CityDao     cityDao();
    public  abstract CountyDao   countyDao();




    private static  PositionDatabase positionDatabase;

    static final Migration MIGRATION_1_2=new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {


             database.execSQL("DROP TABLE City");
        }
    };
    
    private  static PositionDatabase create(Context context){
          return Room.databaseBuilder(context,PositionDatabase.class,DB_NAME)
                  .addMigrations()
                  .allowMainThreadQueries()   //允许主线程操作数据库
                .build();
    };

    public static  PositionDatabase  getInstance(Context context){
            if(positionDatabase==null){
                 synchronized (PositionDatabase.class){
                      if(positionDatabase==null){
                          positionDatabase=PositionDatabase.create(context);
                      }
                 }
            }
            return  positionDatabase;
    }
}
