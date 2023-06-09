package com.coolweather.android.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;

import com.coolweather.android.gson.Weather;
import com.coolweather.android.utils.HttpUtil;
import com.coolweather.android.utils.Utilty;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {
    public AutoUpdateService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();
        AlarmManager alarmManager=(AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour=8*60*60*1000;  //8小时的毫秒数
        long triggerAttime= SystemClock.elapsedRealtime()+anHour;  //系统开机时间向后移8小时
        Intent  i=new Intent(this,AutoUpdateService.class);
        PendingIntent pendingIntent=PendingIntent.getService(this,0,intent,0);
        alarmManager.cancel(pendingIntent);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAttime,pendingIntent);
        return super.onStartCommand(intent, flags, startId);

    }

    private void updateBingPic() {
         String  requestUrl="https://www.dmoe.cc/random.php?return=json";
         HttpUtil.sendRquest(requestUrl, new Callback() {
             @Override
             public void onFailure(@NonNull Call call, @NonNull IOException e) {
                 e.printStackTrace();
             }

             @Override
             public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                       String reponseText=response.body().string();
                       String imgUrl=Utilty.handleImgUrl(reponseText);
                       SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                       editor.putString("bing_pic",imgUrl);
                       editor.apply();
             }
         });
    }

    private void updateWeather() {
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
         String weatherString =sharedPreferences.getString("weather",null);
         if(weatherString!=null){
              Weather weather= Utilty.handleWeatherResponse(weatherString);
              String weatherId=weather.basic.weatherId;
              String requestUrl="http://guolin.tech/api/weather?cityid="+weatherId+"&key=3725fc9dbb8c4d7489566d8580ec0fc4";
              HttpUtil.sendRquest(requestUrl, new Callback() {
                 @Override
                 public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                 }

                 @Override
                 public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                          String responseText=response.body().string();
                          Weather weather=Utilty.handleWeatherResponse(responseText);
                          if(weather!=null && "ok".equals(weather.status)){
                                SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                                editor.putString("weather",responseText);
                                editor.apply();
                     }

                 }
             });

         }

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}