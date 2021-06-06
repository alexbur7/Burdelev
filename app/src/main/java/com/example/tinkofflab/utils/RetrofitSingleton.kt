package com.example.tinkofflab.utils

import com.google.gson.GsonBuilder
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitSingleton {
    private const val BASE_URL = "https://developerslife.ru/"
    private val httpClient = OkHttpClient()
    private val gson = GsonBuilder().create()
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .client(httpClient)
        .baseUrl(BASE_URL)
        .build()

    fun getAppApi(): AppApi {
        return retrofit.create(AppApi::class.java)
    }
}