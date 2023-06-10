package com.coolweather.android.db.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.coolweather.android.db.entity.City;

import java.util.List;

@Dao
public interface CityDao {

    @Query("select * from City")
    List<City> getAllCity();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(City city);

    @Query("select * from City where provinceId=:id")
    List<City> findCityByProvinceId(int id);

    @Query("delete  from City  where 1=1")
    void deleteAll();


    @Query("select * from City where cityName=:cityName")
    City  getCityByName(String cityName);


    @Update(onConflict =  OnConflictStrategy.REPLACE)   //出现重复记录时替换原记录
    int  updateCity(City city);

}
