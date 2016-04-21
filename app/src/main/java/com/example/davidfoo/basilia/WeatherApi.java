package com.example.davidfoo.basilia;

/**
 * @author david.foo
 */

import android.database.Observable;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public  interface  WeatherApi {

  //  @GET ( "/Data/2.5/{Name}" )
  //  public Observable<WeatherEntity> getWeather(@Path("name") String name, @Query("q") String q);
// Callback for the parsed response is the last parameter

      @GET ( "/data/2.5/{Name}" )
      Call<WeatherEntity> getWeather(@Path("Name") String name, @Query("q") String q, @Query("appid") String key);

    @GET ( "/data/2.5/{Name}" )
    rx.Observable<WeatherEntity> getWeather1(@Path("Name") String name, @Query("q") String q, @Query("appid") String key);

//    @GET ( "/data/2.5/{Name}" )
//    Observable<WeatherEntity> getWeatherRx(@Path("Name") String name, @Query("q") String q);

}