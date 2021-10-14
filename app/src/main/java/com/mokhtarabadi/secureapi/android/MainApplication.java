package com.mokhtarabadi.secureapi.android;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mokhtarabadi.secureapi.android.interceptor.SignatureInterceptor;

import java.security.SecureRandom;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Getter
@Slf4j
public class MainApplication extends Application {

    private final String baseUrl = "http://192.168.1.7:4567/api/v1/";
    private final String apiKey = "1234567890asdfghjkl";
    private final String apiSecret = "zxcvbnm,asdfghjklwqertyuiol";

    private Gson gson;
    private OkHttpClient client;
    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        gson = new GsonBuilder().disableHtmlEscaping().setLenient().setPrettyPrinting().create();

        val loggingInterceptor =
                new HttpLoggingInterceptor(log::info).setLevel(HttpLoggingInterceptor.Level.BODY);
        client =
                new OkHttpClient.Builder()
                        .addInterceptor(
                                new SignatureInterceptor(
                                        getApiKey(), getApiSecret(), getGson(), new SecureRandom()))
                        .addInterceptor(loggingInterceptor)
                        .build();

        retrofit =
                new Retrofit.Builder()
                        .baseUrl(getBaseUrl())
                        .client(getClient())
                        .addConverterFactory(GsonConverterFactory.create(getGson()))
                        .build();
    }
}
