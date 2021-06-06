package com.example.tinkofflab.utils

import com.example.tinkofflab.model.ResultContentModel
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path

interface AppApi {

    @GET("latest/{number_page}?json=true")
    fun getLatestGif(@Path("number_page") numberPage:Int):Flowable<ResultContentModel>

    @GET("hot/{number_page}?json=true")
    fun getHotGif(@Path("number_page") numberPage:Int):Flowable<ResultContentModel>

    @GET("top/{number_page}?json=true")
    fun getTopGif(@Path("number_page") numberPage:Int):Flowable<ResultContentModel>
}