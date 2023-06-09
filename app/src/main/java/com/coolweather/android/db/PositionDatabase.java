package com.coolweather.android.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.coolweather.android.db.Dao.CityDao;
import com.coolweather.android.db.Dao.CountyDao;
import com.coolweather.android.db.Dao.ProvinceDao;
import com.coolweather.android.db.entity.City;
import com.coolweather.android.db.entity.County;
import com.coolweather.android.db.entity.Province;

@Database(entities = {Province.class, City.class, County.class},version = 1,exportSchema = false)
public abstract  class PositionDatabase extends RoomDatabase {

    public  abstract ProvinceDao provinceDao();
    public  abstract CityDao     cityDao();
    public  abstract CountyDao   countyDao();
}
