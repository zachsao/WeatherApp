package com.example.zach.weatherapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zach.weatherapp.R
import com.example.zach.weatherapp.data.WeeklyForecast
import com.example.zach.weatherapp.databinding.ForecastItemBinding
import com.example.zach.weatherapp.utils.GlideApp
import java.text.SimpleDateFormat
import java.util.*

class ForecastAdapter(var mData: List<WeeklyForecast>) : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val inflater =  LayoutInflater.from(parent.context)

        val binding = ForecastItemBinding.inflate(inflater,parent,false)

        return ForecastViewHolder(binding)
    }

    override fun getItemCount() = mData.size

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    inner class ForecastViewHolder(var binding: ForecastItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(weeklyForecast: WeeklyForecast){
            binding.dayTextView.text = parseDate(weeklyForecast.dt_txt)
            binding.forecastMaxTempTextview.text = "${weeklyForecast.main.temp_max.toInt()}°"
            binding.forecastMinTempTextview.text = "${weeklyForecast.main.temp_min.toInt()}°"

            GlideApp.with(binding.root.context)
                .load("http://openweathermap.org/img/w/${weeklyForecast.weather[0].icon}.png")
                .error(R.drawable.ic_error)
                .into(binding.forecastIcon)
        }

        private fun parseDate(dt_txt:String):String{
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRENCH)
            val dateFormat2 = SimpleDateFormat("EEE dd MMM", Locale.FRENCH)
            val date = dateFormat.parse(dt_txt)
            return dateFormat2.format(date)
        }
    }
}