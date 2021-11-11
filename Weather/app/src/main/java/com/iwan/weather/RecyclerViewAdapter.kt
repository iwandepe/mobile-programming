package com.iwan.weather

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RecyclerViewAdapter (
    private val dataSet: MutableList<DailyItem>,
    private val rowLayout: Int
):
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val weather: TextView
        val day: TextView
        val humidity: TextView
        val temperature: TextView

        init {
            day = view.findViewById(R.id.tvDay)
            weather = view.findViewById(R.id.tvWeather)
            humidity = view.findViewById(R.id.tvHumidity)
            temperature = view.findViewById(R.id.tvTemperature)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(rowLayout, viewGroup, false)

        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.weather.text = dataSet[position].weather!![0]!!.main
        viewHolder.day.text = this.getDateTime(dataSet[position].dt!!.toLong())
        viewHolder.humidity.text = dataSet[position].humidity.toString() + "%"
        viewHolder.temperature.text = dataSet[position].temp!!.day.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDateTime(timestamp: Long): String? {
        val secondApiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")

        val timestampAsDateString = java.time.format.DateTimeFormatter.ISO_INSTANT
                .format(java.time.Instant.ofEpochSecond(timestamp))

        val nowInEpoch: Long = Instant.now().epochSecond.toLong()

        val date = LocalDate.parse(timestampAsDateString, secondApiFormat)

        if (date.dayOfWeek.toString() == LocalDate.now().dayOfWeek.toString()) {
            return "Today"
        }
        if (nowInEpoch > timestamp) {
            return "Yesterday"
        }
        else {
            return(date.dayOfWeek.toString().toLowerCase().capitalize())
        }
    }

    override fun getItemCount() = dataSet.size

}