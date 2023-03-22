package com.example.mvvm_weather.network;

import com.example.mvvm_weather.features.model.data.WeatherInfoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {
    @GET("weather")
    Call<WeatherInfoResponse> apiWeatherInfoResponse(@Query("id") int cityId, @Query("appid") String APP_ID);
}
