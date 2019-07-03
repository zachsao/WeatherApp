package com.example.zach.weatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zach.weatherapp.data.WeeklyForecast
import com.example.zach.weatherapp.databinding.SubItemHourlyForecastBinding
import com.example.zach.weatherapp.utils.GlideApp
import java.text.SimpleDateFormat
import java.util.*

class HourlyForecastAdapter(var mData: List<WeeklyForecast>): RecyclerView.Adapter<HourlyForecastAdapter.HourlyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SubItemHourlyForecastBinding.inflate(inflater,parent,false)
        return HourlyViewHolder(binding)
    }

    override fun getItemCount() = mData.size

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        holder.bind(mData[position])
    }


    inner class HourlyViewHolder(var binding: SubItemHourlyForecastBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(weeklyForecast: WeeklyForecast) {
            binding.hourTextView.text = parseDate(weeklyForecast.dt_txt)
            binding.temperatureTextview.text = "${weeklyForecast.main.temp.toInt()}"

            GlideApp.with(binding.root.context)
                .load("http://openweathermap.org/img/wn/${weeklyForecast.weather[0].icon}@2x.png")
                .into(binding.weatherIconImageView)
        }

        private fun parseDate(dt_txt:String):String{
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRENCH)
            val dateFormat2 = SimpleDateFormat("HH:mm", Locale.FRENCH)
            val date = dateFormat.parse(dt_txt)
            return dateFormat2.format(date)
        }

    }
}