package com.example.zach.weatherapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zach.weatherapp.R
import com.example.zach.weatherapp.data.WeeklyForecast
import com.example.zach.weatherapp.databinding.ForecastItemBinding

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
            binding.dayTextView.text = weeklyForecast.forecastList[0].date
            binding.forecastIcon.setImageResource(R.drawable.sunny)
            binding.forecastMaxTempTextview.text = "${weeklyForecast.forecastList[0].main.temp_max}"
            binding.forecastMinTempTextview.text = "${weeklyForecast.forecastList[0].main.temp_min}"
        }
    }
}