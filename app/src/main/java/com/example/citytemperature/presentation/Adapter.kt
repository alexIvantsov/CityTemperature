package com.example.citytemperature.presentation

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.citytemperature.R
import com.example.citytemperature.data.city.model.City
import kotlinx.android.synthetic.main.list_item.view.*
import kotlin.math.roundToInt

class Adapter(val context: Context, val items: List<City>, val presenter: CityListPresenter) : RecyclerView.Adapter<Adapter.ViewHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = items[position]
        holder.itemView.cityName.text = city.name
        val distance = if(city.distance != null){
            val formattedDistance = String.format("%.02f", city.distance)
            context.getString(R.string.distance, formattedDistance)
        }else{
            null
        }
        holder.itemView.distance.text = distance
        val weather = city.weather
        val temperature = weather?.main?.temp?.roundToInt()?.toString()
        holder.itemView.temperature.text = if(temperature == null){
            null
        }else{
            context.getString(R.string.temperature, temperature)
        }
        holder.itemView.progressBar.visibility = if(weather == null) View.VISIBLE else View.GONE
        if(weather == null){
            presenter.getWeather(city, position)
        }
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view)
}