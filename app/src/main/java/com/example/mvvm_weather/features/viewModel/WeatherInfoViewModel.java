package com.example.mvvm_weather.features.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mvvm_weather.common.RequestCompleteListener;
import com.example.mvvm_weather.features.model.WeatherInfoModel;
import com.example.mvvm_weather.features.model.data.City;
import com.example.mvvm_weather.features.model.data.WeatherDataModel;
import com.example.mvvm_weather.features.model.data.WeatherInfoResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class WeatherInfoViewModel extends ViewModel {
//    private LiveData<List<City>> cityListLiveData = MutableLiveData<List<City>>();
    private MutableLiveData<List<City>> cityListLiveData;
    private MutableLiveData<String> cityListFailureLiveData;
    private MutableLiveData<WeatherDataModel> weatherInfoLiveData;
    private MutableLiveData<String> weatherInfoFailureLiveData;
    private MutableLiveData<Boolean> progressBarLiveData;



    public void getCityList(WeatherInfoModel model){
        model.getCityList(new RequestCompleteListener<List<City>>() {
            @Override
            public void onRequestSuccess(List<City> data) {
                cityListLiveData.postValue(data);
            }

            @Override
            public void onRequestFailed(String errorMessage) {
                cityListFailureLiveData.postValue(errorMessage);
            }
        });
    }


    public void getWeatherInfo(int cityId, WeatherInfoModel model){
        progressBarLiveData.postValue(true);

        model.getWeatherInformation(cityId, new RequestCompleteListener<WeatherInfoResponse>() {
            @Override
            public void onRequestSuccess(WeatherInfoResponse data) {

                WeatherDataModel weatherDataModel = new WeatherDataModel();
                weatherDataModel.setDateTime(unixTimeStampToDateTimeString(data.getDt()));
                weatherDataModel.setTemperature(kelvinToCelsius(data.getMain().getTemp()));
                weatherDataModel.setCityAndCountry(data.getName() + data.getSys().getCountry());
                weatherDataModel.setWeatherConditionIconUrl("http://openweathermap.org/img/w/"+ data.getWeather().get(0).getIcon().toString()+".png");
                weatherDataModel.setWeatherConditionIconDescription(data.getWeather().get(0).getDescription());
                weatherDataModel.setHumidity(data.getMain().getHumidity().toString()+"%");
                weatherDataModel.setPressure(data.getMain().getPressure().toString()+" mBar");
                weatherDataModel.setVisibility(String.valueOf(data.getVisibility()/1000.0)+" KM");
                weatherDataModel.setSunrise(unixTimeStampToTimeString(data.getSys().getSunrise()));
                weatherDataModel.setSunset(unixTimeStampToTimeString(data.getSys().getSunset()));

                progressBarLiveData.postValue(false);

                weatherInfoLiveData.postValue(weatherDataModel);
            }

            @Override
            public void onRequestFailed(String errorMessage) {
                progressBarLiveData.postValue(false);
                weatherInfoFailureLiveData.postValue(errorMessage);
            }
        });
    }





    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String unixTimeStampToDateTimeString(Integer dt){
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(dt * 1000);

            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd MMM, yyyy - hh:mm a", Locale.ENGLISH);
            outputDateFormat.setTimeZone(TimeZone.getDefault());
            return outputDateFormat.format(calendar.getTime());
        }catch (Exception e){
            e.printStackTrace();
        }
        return dt.toString();
    }

    public String unixTimeStampToTimeString(Integer dt){
        try{
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(dt * 1000);

            SimpleDateFormat outputDateFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            outputDateFormat.setTimeZone(TimeZone.getDefault());
            return outputDateFormat.format(calendar.getTime());
        }catch (Exception e){
            e.printStackTrace();
        }
        return dt.toString();
    }

    public String kelvinToCelsius(Double temp){
        return String.valueOf(temp- 273.15);
    }

}
