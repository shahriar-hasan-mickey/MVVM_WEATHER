package com.example.mvvm_weather.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public String  BASE_URL = "https://samples.openweathermap.org/data/2.5/";

    private Retrofit retrofit = null;

    public Retrofit getRetrofit() {
        if(retrofit == null)
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        return retrofit;
    }
}
