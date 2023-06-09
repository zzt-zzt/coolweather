package com.coolweather.android.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Province {
     @PrimaryKey(autoGenerate = true)
     @NonNull
     private  int id;

     @NonNull
     private String provinceName;
     @NonNull
     private  int provinceCode;



     public int getId() {
          return id;
     }

     public void setId(int id) {
          this.id = id;
     }

     @NonNull
     public String getProvinceName() {
          return provinceName;
     }

     public void setProvinceName(@NonNull String provinceName) {
          this.provinceName = provinceName;
     }

     public int getProvinceCode() {
          return provinceCode;
     }

     public void setProvinceCode(int provinceCode) {
          this.provinceCode = provinceCode;
     }
}
