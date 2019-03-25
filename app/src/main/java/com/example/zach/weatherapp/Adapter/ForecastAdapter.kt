package com.example.zach.weatherapp.Adapter

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
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class ForecastAdapter(var mData: List<WeeklyForecast>) : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val inflater =  LayoutInflater.from(parent.context)

        val binding = ForecastItemBinding.inflate(inflater,parent,false)

        return ForecastViewHolder(binding)
    }

    override fun getItemCount() = mData.size

    fun organizeList(length: Int,L: List<WeeklyForecast>): Array<MutableList<WeeklyForecast>>{
        val days = Array(length, {mutableListOf<WeeklyForecast>()})

        days[0].add(L[0])
        var k = 1
        for(i in 0..days.size){
            while(k<L.size && L[k].dt_txt.equals(L[k-1].dt_txt)){
                days[i].add(L[k])
                k++
            }
            if(i+1<days.size)
                days[i+1].add(L[k])
            k++
        }
        return days
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {

        /*val three_pm_forecast_list = mData.filter { it.dt_txt.contains("15:00") }

        if(position<three_pm_forecast_list.size){
            val forecast = three_pm_forecast_list[position]

            val days = organizeList(three_pm_forecast_list.size,mData)//.filter { it.size > 5 }
            Timber.i(Arrays.toString(days))


            holder.bind(forecast)

            holder.binding.root.setOnClickListener{
                val expanded = forecast.expanded
                forecast.expanded = !expanded
                notifyItemChanged(position)
            }

            holder.binding.subItemRecyclerView.apply {
                layoutManager = LinearLayoutManager(holder.binding.root.context, HORIZONTAL,false)
                adapter = HourlyForecastAdapter(days[position])

            }
        }*/

        val forecast = mData[position]
        holder.bind(forecast)

        holder.binding.root.setOnClickListener {
            val expanded = forecast.expanded
            forecast.expanded = !expanded
            notifyItemChanged(position)
        }
    }

    inner class ForecastViewHolder(var binding: ForecastItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(weeklyForecast: WeeklyForecast){
            binding.dayTextView.text = parseDate(weeklyForecast.dt_txt)
            binding.forecastMaxTempTextview.text = "${weeklyForecast.main.temp_max.toInt()}°"
            binding.forecastMinTempTextview.text = "${weeklyForecast.main.temp_min.toInt()}°"

            val expanded = weeklyForecast.expanded
            binding.subItemRecyclerView.visibility = when(expanded){
                true -> View.VISIBLE
                false -> View.GONE
            }

            binding.subItemRecyclerView.apply {
                layoutManager = LinearLayoutManager(binding.root.context, HORIZONTAL,false)
                adapter = HourlyForecastAdapter(mData)

            }

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