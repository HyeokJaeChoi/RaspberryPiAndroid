package com.hyeok.raspberrypiproject.model

import com.google.gson.annotations.SerializedName

data class TempHumi (

    @SerializedName("temperature")
    val temperature: Int,
    @SerializedName("humidity")
    val humidity: Int

)