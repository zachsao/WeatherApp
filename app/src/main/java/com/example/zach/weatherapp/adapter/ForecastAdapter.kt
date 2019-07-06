package com.example.zach.weatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.HORIZONTAL
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zach.weatherapp.R
import com.example.zach.weatherapp.data.WeeklyForecast
import com.example.zach.weatherapp.databinding.ForecastItemBinding
import com.example.zach.weatherapp.utils.GlideApp
import java.text.SimpleDateFormat
import java.util.*

class ForecastAdapter(var mData: List<List<WeeklyForecast>>) : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val inflater =  LayoutInflater.from(parent.context)

        val binding = ForecastItemBinding.inflate(inflater,parent,false)

        return ForecastViewHolder(binding)
    }

    override fun getItemCount() = mData.size

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {

        val day = mData[position]

        val daily_max_temp = day.maxBy { it.main.temp_max }
        val daily_min_temp = day.minBy { it.main.temp_min }

        daily_max_temp!!.main.temp_min = daily_min_temp!!.main.temp_min


        val forecast = daily_max_temp
        holder.bind(forecast)

        holder.binding.root.setOnClickListener {
            val expanded = forecast.expanded
            forecast.expanded = !expanded
            notifyItemChanged(position)
        }

        holder.binding.subItemRecyclerView.apply {
            layoutManager = LinearLayoutManager(holder.binding.root.context, HORIZONTAL,false)
            adapter = HourlyForecastAdapter(day)

        }
    }

    inner class ForecastViewHolder(var binding: ForecastItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(weeklyForecast: WeeklyForecast){
            binding.dayTextView.text = parseDate(weeklyForecast.dt_txt)
            binding.forecastMaxTempTextview.text = "${weeklyForecast.main.temp_max.toInt()}°"
            binding.forecastMinTempTextview.text = "${weeklyForecast.main.temp_min.toInt()}°"
            binding.descriptionTextView.text = weeklyForecast.weather[0].description

            val expanded = weeklyForecast.expanded
            binding.subItemRecyclerView.visibility = when(expanded){
                true -> View.VISIBLE
                false -> View.GONE
            }

            GlideApp.with(binding.root.context)
                .load("http://openweathermap.org/img/wn/${weeklyForecast.weather[0].icon}@2x.png")
                .error(R.drawable.ic_error)
                .into(binding.forecastIcon)
        }

        private fun parseDate(dt_txt:String):String{
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRENCH)
            val dateFormat2 = SimpleDateFormat("EEE dd MMM")
            val date = dateFormat.parse(dt_txt)
            return dateFormat2.format(date)
        }
    }
}