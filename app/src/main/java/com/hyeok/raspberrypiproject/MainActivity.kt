package com.hyeok.raspberrypiproject

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hyeok.raspberrypiproject.api.TempHumiApi
import com.hyeok.raspberrypiproject.model.TempHumi
import com.hyeok.raspberrypiproject.util.NetworkUtil
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private val tempList by lazy { mutableListOf<Int>() }
    private val humiList by lazy { mutableListOf<Int>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NetworkUtil.retrofit
            .create(TempHumiApi::class.java)
            .getTempHumis()
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.d("MainActivity", response.isSuccessful.toString())
                    if(response.isSuccessful) {
                        response.body()?.let {
                            val replacedJson = it.replace("[\"", "[")
                                .replace("\"]", "]")
                                .replace("}\"", "}")
                                .replace("\"{", "{")
                            val type = object : TypeToken<List<TempHumi>>(){}.type
                            val tempHumiList = Gson().fromJson<List<TempHumi>>(replacedJson, type)

                            for(i in tempHumiList.indices) {
                                tempList.add(tempHumiList[i].temperature)
                                humiList.add(tempHumiList[i].humidity)
                            }

                            setChart()
                        }
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("MainActivity", t.message)
                }
            })
    }

    private fun setChart() {
        val tempEntries = mutableListOf<BarEntry>()
        val humiEntries = mutableListOf<BarEntry>()

        for(i in tempList.indices) {
            tempEntries.add(BarEntry(i.toFloat(), tempList[i].toFloat()))
            humiEntries.add(BarEntry(i.toFloat(), humiList[i].toFloat()))
        }

        val tempDataSet = BarDataSet(tempEntries, "온도")
        val humiDataSet = BarDataSet(humiEntries, "습도")
        val tempBarData = BarData(tempDataSet)
        val humiBarData = BarData(humiDataSet)

        tempBarData.barWidth = 0.9f
        humiBarData.barWidth = 0.9f

        temperature_chart.data = tempBarData
        humidity_chart.data = humiBarData
        temperature_chart.invalidate()
        humidity_chart.invalidate()

    }
}
