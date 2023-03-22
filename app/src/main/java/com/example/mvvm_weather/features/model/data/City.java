package com.example.mvvm_weather.features.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class City {
    @SerializedName("id")
    @Expose
    private int id = 0;
    @Expose
    @SerializedName("name")
    private String name = "";

}
