package com.example.melificent.myqianqi.NetService;

import com.example.melificent.myqianqi.Bean.WeatherBean.WeatherAllDayRealTime;
import com.example.melificent.myqianqi.Utils.GlobalContants;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Headers;

/**
 * Created by p on 2017/3/14.
 */

public interface WeatherApiService {
    @Headers(GlobalContants.HeadsSet)
    @GET("?areaid=101080705&key=482670f5b8546fb893c1a6f157b184fb")
    Call<WeatherAllDayRealTime> getWeatherData(
    );
}
