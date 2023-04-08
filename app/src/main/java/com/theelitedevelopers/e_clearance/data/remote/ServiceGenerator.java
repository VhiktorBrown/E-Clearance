package com.theelitedevelopers.e_clearance.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

//
// Created on 3/18/2021.
// Copyright (c) 2021 Syticks. All rights reserved.
//
public class
ServiceGenerator {

    private static final String TAG = "ServiceGenerator";
    private static final String PAY_STACK_BASE_URL = "https://api.paystack.co/";
    private static Retrofit retrofit = null, payStackRetrofit = null;
    private static ServiceGenerator instance;


    public static synchronized ServiceGenerator getInstance() {
        if (instance == null) {
            instance = new ServiceGenerator();
        }
        return instance;
    }

    public static Retrofit getRetrofit() {
        return payStackRetrofit;
    }

    Gson gson = new GsonBuilder()
            .serializeNulls()
            .setLenient()
            .create();

    private ServiceGenerator() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build();

        payStackRetrofit = new Retrofit.Builder()
                .baseUrl(PAY_STACK_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public ApiInterface getPayStackApi(){
        return payStackRetrofit.create(ApiInterface.class);
    }
}
