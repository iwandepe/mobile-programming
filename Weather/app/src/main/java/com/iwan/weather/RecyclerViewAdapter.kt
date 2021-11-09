package com.iwan.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter (
    private val dataSet: MutableList<WeatherItem>,
    private val rowLayout: Int
):
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val weather: TextView
        val description: TextView

        init {
            weather = view.findViewById(R.id.tvWeather)
            description = view.findViewById(R.id.tvDescription)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(rowLayout, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.weather.text = dataSet[position].main
        viewHolder.description.text = dataSet[position].description
    }

    override fun getItemCount() = dataSet.size

}