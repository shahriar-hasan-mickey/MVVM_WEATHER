package com.example.mvvm_weather.features.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mvvm_weather.R;
import com.example.mvvm_weather.databinding.ActivityMainBinding;
import com.example.mvvm_weather.features.model.WeatherInfoModel;
import com.example.mvvm_weather.features.model.WeatherInfoModelImpl;
import com.example.mvvm_weather.features.model.data.City;
import com.example.mvvm_weather.features.model.data.WeatherDataModel;
import com.example.mvvm_weather.features.viewModel.WeatherInfoViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    WeatherInfoViewModel viewModel;
    WeatherInfoModel model;

    List<City> cityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.color.colorPrimaryDark));
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        binding.layoutInput.btnViewWeather.setBackground(getDrawable(R.color.colorPrimaryDark));

        model = new WeatherInfoModelImpl(getApplicationContext());
        viewModel = new ViewModelProvider(this).get(WeatherInfoViewModel.class);

        setLiveDataListeners();
        setViewClickListener();

        viewModel.getCityList(model);

    }

    private void setViewClickListener() {
        binding.layoutInput.btnViewWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.getWeatherInfo(cityList.get(binding.layoutInput.spinner.getSelectedItemPosition()).getId(), model);
            }
        });
    }

    private void setLiveDataListeners() {

        Log.i("data", "here");
        viewModel.cityListLiveData.observe(this, new Observer<List<City>>() {
            @Override
            public void onChanged(List<City> cityList) {
                Log.i("data", "here1");
                setCityListSpinner(cityList);
                Log.i("data", "here2");
            }
        });

        viewModel.cityListFailureLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.progressBarLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loading) {
                if(loading){
                    binding.progressBar.setVisibility(View.VISIBLE);
                }else{
                    binding.progressBar.setVisibility(View.GONE);
                }
            }
        });

        viewModel.weatherInfoLiveData.observe(this, new Observer<WeatherDataModel>() {
            @Override
            public void onChanged(WeatherDataModel weatherDataModel) {
                setWeatherInfo(weatherDataModel);
            }
        });

        viewModel.weatherInfoFailureLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                binding.outputGroup.setVisibility(View.GONE);
                binding.tvErrorMessage.setVisibility(View.VISIBLE);
                binding.tvErrorMessage.setText(errorMessage);
            }
        });
    }

    private void setWeatherInfo(WeatherDataModel weatherDataModel) {
        binding.outputGroup.setVisibility(View.VISIBLE);
        binding.tvErrorMessage.setVisibility(View.GONE);

        binding.layoutWeatherBasic.tvDateTime.setText(weatherDataModel.getDateTime());
        binding.layoutWeatherBasic.tvTemperature.setText(weatherDataModel.getTemperature());
        binding.layoutWeatherBasic.tvCityCountry.setText(weatherDataModel.getCityAndCountry());
        Glide.with(this).load(weatherDataModel.getWeatherConditionIconUrl()).into(binding.layoutWeatherBasic.ivWeatherCondition);
        binding.layoutWeatherBasic.tvWeatherCondition.setText(weatherDataModel.getWeatherConditionIconDescription());

        binding.layoutWeatherAdditional.tvHumidityValue.setText(weatherDataModel.getHumidity());
        binding.layoutWeatherAdditional.tvPressureValue.setText(weatherDataModel.getPressure());
        binding.layoutWeatherAdditional.tvVisibilityValue.setText(weatherDataModel.getVisibility());

        binding.layoutSunsetSunrise.tvSunriseTime.setText(weatherDataModel.getSunrise());
        binding.layoutSunsetSunrise.tvSunsetTime.setText(weatherDataModel.getSunset());
    }

    private void setCityListSpinner(List<City> city_list) {
        this.cityList = city_list;

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                convertToCityNameList(city_list)
        );

        binding.layoutInput.spinner.setAdapter(arrayAdapter);
    }



    private List<String> convertToCityNameList(List<City> city_list){

        List<String> cityNameList = new ArrayList<>();
        for(City i : city_list){
            cityNameList.add(i.getName());
        }
        Log.i("DEBUG", ">>>>>> IN CONVERT TO CITY NAME LIST");
        return cityNameList;

    }
}