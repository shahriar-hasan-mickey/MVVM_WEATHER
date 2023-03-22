package com.example.mvvm_weather.features.model;

import android.content.Context;

import com.example.mvvm_weather.common.RequestCompleteListener;
import com.example.mvvm_weather.features.model.data.City;
import com.example.mvvm_weather.features.model.data.WeatherInfoResponse;
import com.example.mvvm_weather.network.API;
import com.example.mvvm_weather.network.RetrofitClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherInfoModelImpl implements WeatherInfoModel{

    Context context;

    public WeatherInfoModelImpl(Context context) {
        this.context = context;
    }


    @Override
    public void getCityList(RequestCompleteListener<List<City>> callback) {
        try {
            InputStream is = context.getAssets().open("city_list.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String jsonFileString = new String(buffer, StandardCharsets.UTF_8);

            Gson gson = new Gson();
            Type listCityType = new TypeToken<List<City>>(){}.getType();

            List<City> cityList = gson.fromJson(jsonFileString, listCityType);


            callback.onRequestSuccess(cityList);
        }catch (IOException e){
            e.printStackTrace();
            callback.onRequestFailed(e.getLocalizedMessage());
        }
    }

    @Override
    public void getWeatherInformation(int cityId, RequestCompleteListener<WeatherInfoResponse> callback) {
        API api = new RetrofitClient().getRetrofit().create(API.class);

        /******************************************************************************************/
        String APP_ID = "b6907d289e10d714a6e88b30761fae22";
        /******************************************************************************************/

        Call<WeatherInfoResponse> call = api.apiWeatherInfoResponse(cityId, APP_ID);

        call.enqueue(new Callback<WeatherInfoResponse>() {
            @Override
            public void onResponse(Call<WeatherInfoResponse> call, Response<WeatherInfoResponse> response) {
                if(response.body()!=null){
                    callback.onRequestSuccess(response.body());
                }else {
                    callback.onRequestFailed(response.message());
                }
            }

            @Override
            public void onFailure(Call<WeatherInfoResponse> call, Throwable t) {
                callback.onRequestFailed(t.getLocalizedMessage());
            }
        });
    }
}
