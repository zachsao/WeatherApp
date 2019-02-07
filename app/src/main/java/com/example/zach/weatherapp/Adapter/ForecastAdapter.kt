package com.example.zach.weatherapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.zach.weatherapp.BR
import com.example.zach.weatherapp.R
import com.example.zach.weatherapp.data.City
import com.example.zach.weatherapp.databinding.CityListItemBinding
import com.example.zach.weatherapp.ui.ListFragmentDirections
import kotlinx.android.synthetic.main.city_list_item.view.*
import timber.log.Timber

class ForecastAdapter(var myDataset: List<City>) :
    RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    var context:Context?=null

    inner class ForecastViewHolder(var binding: CityListItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(boundCity: City){
            with(binding){
                //cityItem = boundCity
                cityNameTextview.text = boundCity.name
                cityForecastTextview.text = boundCity.weather[0].description
                maxTemperatureTextview.text = "${boundCity.main.temp_max}°"
                minTemperatureTextview.text = "${boundCity.main.temp_min}°"

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
                                    viewType: Int): ForecastAdapter.ForecastViewHolder {
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