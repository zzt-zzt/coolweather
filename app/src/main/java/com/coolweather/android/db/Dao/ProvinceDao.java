package com.coolweather.android.db.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.coolweather.android.db.entity.Province;

import java.util.List;

@Dao
public interface ProvinceDao {
     @Query("select *  from Province")
      List<Province> getAllProvince();

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     void   insert(Province  province);

     @Query("delete  from Province where 1=1")
     void deleteAll();



}
