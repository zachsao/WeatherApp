package com.example.zach.weatherapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.zach.weatherapp.data.City
import com.example.zach.weatherapp.databinding.CityListItemBinding
import com.example.zach.weatherapp.ui.ListFragmentDirections
import timber.log.Timber

class CityAdapter(var myDataset: List<City>) :
    RecyclerView.Adapter<CityAdapter.ForecastViewHolder>() {

    var context:Context?=null

    inner class ForecastViewHolder(var binding: CityListItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(boundCity: City){
            with(binding){
                //cityItem = boundCity
                cityNameTextview.text = boundCity.name
                cityForecastTextview.text = boundCity.weather[0].description
                maxTemperatureTextview.text = "${boundCity.main.temp_max.toInt()}°"
                minTemperatureTextview.text = "${boundCity.main.temp_min.toInt()}°"

                Glide.with(root.context)
                    .load("http://openweathermap.org/img/w/${boundCity.weather[0].icon}.png")
                    .into(forceastImageView)

                container.setOnClickListener{ view: View ->
                    Timber.d("Clicked on city %s",boundCity.name)
                    Navigation.findNavController(view).navigate(ListFragmentDirections.actionListFragmentToForecastDetailsFragment(boundCity.id))}
            }
        }
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): CityAdapter.ForecastViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)

        val binding = CityListItemBinding.inflate(inflater,parent,false)

        return ForecastViewHolder(binding)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val city = myDataset[position]
        holder.bind(city)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}