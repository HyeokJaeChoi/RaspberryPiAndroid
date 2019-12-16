package com.hyeok.raspberrypiproject.api

import com.hyeok.raspberrypiproject.model.TempHumi
import com.hyeok.raspberrypiproject.model.Todo
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface TempHumiApi {

    @Headers("Accept: Application/json")
    @GET("prod")
    fun getTempHumis(): Call<String>

}