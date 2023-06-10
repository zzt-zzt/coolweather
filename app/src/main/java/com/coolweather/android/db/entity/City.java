package com.coolweather.android.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class City {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private  int id;

    @NonNull
    private  String  cityName;
    @NonNull
    private  int    cityCode;

    @NonNull
    private  int provinceId;


    private  String weatherId;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getCityName() {
        return cityName;
    }

    public void setCityName(@NonNull String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    @NonNull
    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(@NonNull String weatherId) {
        this.weatherId = weatherId;
    }
}
