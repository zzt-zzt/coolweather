package com.coolweather.android.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Query;

import com.coolweather.android.R;
import com.coolweather.android.WeatherActivity;
import com.coolweather.android.WeatherApplication;
import com.coolweather.android.db.Dao.CityDao;
import com.coolweather.android.db.Dao.CountyDao;
import com.coolweather.android.db.Dao.ProvinceDao;
import com.coolweather.android.db.entity.City;
import com.coolweather.android.db.entity.County;
import com.coolweather.android.db.entity.Province;
import com.coolweather.android.utils.HttpUtil;
import com.coolweather.android.utils.Utilty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChooseAreaFragment  extends Fragment {
      public  static  final  int LEVEL_PROVINCE=0;
      public  static  final  int LEVEL_CITY=1;
      public  static  final  int LEVEL_COUNTY=2;

      private TextView tv_title;
      private Button   btn_back;
      private ListView lv_position;
      private ProgressDialog progressDialog;
      private ArrayAdapter<String> adapter;

      private  List<String>  dataList=new ArrayList<>();
      private List<Province>  provinceList;
      private  List<City> cityList;
      private  List<County>  countyList;
      private ProvinceDao  provinceDao;
      private CityDao     cityDao;
      private CountyDao  countyDao;

      private   int currentLevel;


      private  Province selectedProvince;
      private  City    selectedCity;


      @Nullable
      @Override
      public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            initDao();
            View   view=inflater.inflate(R.layout.choose_area,container,false);
            tv_title=view.findViewById(R.id.tv_title);
            btn_back=view.findViewById(R.id.btn_back);
            lv_position=view.findViewById(R.id.lv_position);
            adapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,dataList);
            lv_position.setAdapter(adapter);

             return  view;
      }
    private void initDao(){
          provinceDao= WeatherApplication.getInstance().getPositionDatabase().provinceDao();
          cityDao=WeatherApplication.getInstance().getPositionDatabase().cityDao();
          countyDao=WeatherApplication.getInstance().getPositionDatabase().countyDao();
    }
      @Override
      public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            lv_position.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                  @Override
                  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                           if(currentLevel==LEVEL_PROVINCE){
                                  selectedProvince=provinceList.get(position);
                                  queryCities();
                           }else if(currentLevel==LEVEL_CITY){
                                  selectedCity=cityList.get(position);
                                  queryCountys();
;                           }else if(currentLevel==LEVEL_COUNTY){
                                String weatherId=countyList.get(position).getWeatherId();
                                Intent intent=new Intent(getActivity(), WeatherActivity.class);
                                intent.putExtra("weather_id",weatherId);
                                startActivity(intent);
                                getActivity().finish();
                           }
                  }
            });

            btn_back.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                         if(currentLevel==LEVEL_COUNTY){
                                queryCities();
                         }else if(currentLevel==LEVEL_CITY){
                                queryProvinces();
                         }
                  }
            });
            queryProvinces();
      }


      private void queryCountys() {
          dataList.clear();
          tv_title.setText(selectedCity.getCityName());
          btn_back.setVisibility(View.VISIBLE);
          countyList=countyDao.findCountyByCityId(selectedCity.getId());
          if(countyList.size()>0){

               for(int i=0;i<countyList.size();i++){
                    dataList.add(countyList.get(i).getCountyName());
                    adapter.notifyDataSetChanged();
                    lv_position.setSelection(0);
                    currentLevel=LEVEL_COUNTY;
               }
          }else{
                String  address="http://guolin.tech/api/china/"+selectedProvince.getProvinceCode()+"/"+selectedCity.getCityCode();
                queryFromServer(address,"county");
          }
      }
    //先从数据库里面查找，没有再去网上查找
      private void queryProvinces() {
            dataList.clear();
           tv_title.setText("中国");
           btn_back.setVisibility(View.GONE);
           provinceList=provinceDao.getAllProvince();
           if(provinceList.size()>0){

               for(int i=0;i<provinceList.size();i++){
                   dataList.add(provinceList.get(i).getProvinceName());
               }
               adapter.notifyDataSetChanged();
               lv_position.setSelection(0);
               currentLevel=LEVEL_PROVINCE;
           }else{
                String address="http://guolin.tech/api/china";
                queryFromServer(address,"province");
           }

      }

    private void queryCities() {
        dataList.clear();
        tv_title.setText(selectedProvince.getProvinceName());
        btn_back.setVisibility(View.VISIBLE);
        cityList=cityDao.findCityByProvinceId(selectedProvince.getId());
        if(cityList.size()>0){

             for(int i=0;i<cityList.size();i++){
                  dataList.add(cityList.get(i).getCityName());
                  adapter.notifyDataSetChanged();
                  lv_position.setSelection(0);
                  currentLevel=LEVEL_CITY;
             }
        }else{
             String address="http://guolin.tech/api/china/"+selectedProvince.getProvinceCode();
             queryFromServer(address,"city");
        }

    }
    private void queryFromServer(String address, String type) {
                 showProgressDialog();
        HttpUtil.sendRquest(address, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();

                        }
                    });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                  String resonseText=response.body().string();
                  Log.d("ChooseArea",resonseText);
                  boolean result=false;
                  if("province".equals(type)){
                        result= Utilty.handleProvinceResponse(resonseText);
                  }else if("city".equals(type)){
                        result=Utilty.handleCityResponse(resonseText,selectedProvince.getId());
                  }else if("county".equals(type)){
                       result=Utilty.handleCountyResponse(resonseText,selectedCity.getId());
                  }
                Log.d("ChooseArea",result+"");
                  if(result){

                       getActivity().runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               closeProgressDialog();
                               if("province".equals(type)){
                                   queryProvinces();
                               }else if("city".equals(type)){
                                   queryCities();
                               }else if("county".equals(type)){
                                   queryCountys();
                               }
                           }
                       });
                  }
            }
        });
      }

      private  void showProgressDialog(){
            if(progressDialog==null){
                 progressDialog=new ProgressDialog(getActivity());
                 progressDialog.setMessage("正在加载....");
                 progressDialog.setCancelable(false);
            }
            progressDialog.show();
      }

      private  void  closeProgressDialog(){
            if(progressDialog!=null){
                 progressDialog.dismiss();
            }
      }


}
