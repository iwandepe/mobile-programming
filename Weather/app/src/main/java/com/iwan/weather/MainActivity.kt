package com.iwan.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    var dataSet: MutableList<WeatherItem> = mutableListOf()
    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerViewAdapter = RecyclerViewAdapter(dataSet, R.layout.item_weather)
        recyclerView = findViewById(R.id.rvWeather)
        recyclerView.adapter = recyclerViewAdapter

        val networkServices = DataServices.create()
        val call = networkServices.getWeather()

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                println("On Failure")
                println(t.message)
            }

            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                println("On Response")
                if (response.body() != null) {
                    var data: WeatherResponse = response.body()!!
                    if (data!!.hourly!!.isNotEmpty()) {
                        println(data)
                        var newData: MutableList<WeatherItem> = mutableListOf()
                        for (hourlyData in data!!.hourly!!) {
                            newData.add(hourlyData!!.weather!!.first()!!)
                        }
                        dataSet.addAll(newData)
                    }
                    println(dataSet)
                    if (dataSet != null) {
                        println("Dataset isnt null")
                        recyclerViewAdapter.notifyDataSetChanged()
                    }
                } else {
                    println("response body null")
                }
            }
        })
    }
}