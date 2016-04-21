package com.example.davidfoo.basilia;

import android.content.Intent;
import android.database.Observable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
/*
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
*/
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;

import java.io.IOException;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit.GsonConverterFactory;

import retrofit.RxJavaCallAdapterFactory;
import retrofit.Call;
import retrofit.Callback;

import retrofit.Response;
import retrofit.Retrofit;

import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = new Intent(this,ServiceEngine.class);
        startService(intent);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

       // finish();
        // Replaces setLogLevel

        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();
        // JSONのパーサー
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();

        // RestAdapterの生成
        final Retrofit adapter = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
               // .client(client)

                //.setLog(new AndroidLog("=NETWORK="))
                .build();

        //setup for accessing the api without Rx
      // adapter.create(WeatherApi.class).getWeather("weather", "Kuala Lumpur,my&amp;appid=44db6a862fba0b067b1930da0d769e98");
        // RestAdapterの生成


        WeatherApi apiService = adapter.create(WeatherApi.class);

        //running without Rxjava
   //    final Call<WeatherEntity> call = apiService.getWeather("weather", "Kuala Lumpur,my","44db6a862fba0b067b1930da0d769e98");
/*
       final Call<WeatherEntity> call = apiService.getWeather("weather", "Kuala Lumpur,my", "44db6a862fba0b067b1930da0d769e98");

        call.enqueue(new Callback<WeatherEntity>() {


            @Override
            public void onResponse(Response<WeatherEntity> response) {
                Log.d("QWER", " response " + response.body().getWeather().size());
            }

            @Override
            public void onFailure(Throwable t) {

            }

        });
*/

        rx.Observer observer = new Observer<WeatherEntity>() {
            @Override
            public void onCompleted() {
                Log.d("QWER", " Obesrver onCompleted()");
                //必要な情報を取り出して画面に表示してください。
            }

            @Override
            public void onError(Throwable e) {
                Log.e("QWER", "Error : " + e.toString());
            }

            @Override
            public void onNext(WeatherEntity weather) {
                Log.d("QWER", "onNext()");
                Log.d("QWER", "onNext():: "+weather.getWeather().get(0).description);
            }
        };

        rx.Observable<WeatherEntity> obv = apiService.getWeather1("weather", "Kuala Lumpur,my", "44db6a862fba0b067b1930da0d769e98");
        obv.observeOn(AndroidSchedulers.mainThread())
                //.subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer);


        /*
        rx.Observable.create(new rx.Observable.OnSubscribe<WeatherEntity>() {

            @Override
            public void call(Subscriber<? super WeatherEntity> subscriber) {
                try {
                    Response response = call.execute();
                    //subscriber.onNext(response.raw());
                   // subscriber.onNext(response);
                   // subscriber.onCompleted();
                    if (!response.isSuccess()) subscriber.onError(new Exception("error"));
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WeatherEntity>() {
                    @Override
                    public void onCompleted() {
                        Log.d("QWER", "QWER onCompleted Rx");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(WeatherEntity weatherEntity) {
                        Log.d("QWER", "QWER onNext RX");
                    }

                });
                */
        /*
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // RestAdapterの生成
        Retrofit adapterRx = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                        //.setLog(new AndroidLog("=NETWORK="))
                .build();

        adapterRx.create(WeatherApi.class).getWeather("weather", "Tokyo,jp");
        WeatherApi apiServiceRx = adapterRx.create(WeatherApi.class);
        //running with Rxjava
        String username = "sarahjean";
        Observable<WeatherEntity> callRx = apiServiceRx.getWeatherRx("weather", "Tokyo,jp");
        Subscription subscription = callRx
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WeatherEntity>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(WeatherEntity weatherEntity) {

                    }
                });
                */
        // 非同期処理の実行
    /*    adapter.create(WeatherApi.class).getWeather("weather", "Tokyo,jp")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observable<WeatherEntity>() {
                    @Override
                    public void onCompleted() {
                        Log.d("MainActivity", "onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("MainActivity", "Error : " + e.toString());
                    }

                    @Override
                    public void onNext(WeatherEntity weather) {
                        Log.d("MainActivity", "onNext()");
                        if (weather != null) {
                    //        ((TextView) findViewById(R.id.text)).setText(weather.weather.get(0).main);
                        }
                    }
                });
    */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
