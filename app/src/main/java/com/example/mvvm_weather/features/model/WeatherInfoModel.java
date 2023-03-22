package com.example.mvvm_weather.features.model;

import com.example.mvvm_weather.common.RequestCompleteListener;
import com.example.mvvm_weather.features.model.data.City;
import com.example.mvvm_weather.features.model.data.WeatherInfoResponse;

import java.util.List;

public interface WeatherInfoModel {
    void getCityList(RequestCompleteListener<List<City>> callback);
    void getWeatherInformation(int cityId, RequestCompleteListener<WeatherInfoResponse> callback);
}
