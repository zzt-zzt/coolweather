package com.coolweather.android.db.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.coolweather.android.db.entity.County;

import java.util.List;

@Dao
public interface CountyDao {

    @Query("select * from County")
    List<County>  getAllCounty();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(County county);

    @Query("select *  from County where cityId=:id")
    List<County> findCountyByCityId(int id);

    @Query("delete  from County where 1=1")
    void deleteAll();
}
