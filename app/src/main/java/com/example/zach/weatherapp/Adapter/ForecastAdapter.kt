package com.example.zach.weatherapp.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.zach.weatherapp.R
import com.example.zach.weatherapp.data.City
import com.example.zach.weatherapp.ui.ListFragmentDirections
import kotlinx.android.synthetic.main.city_list_item.view.*

class ForecastAdapter(var myDataset: List<City>) :
    RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    companion object {
        private val TAG = "ForecastAdapter"
    }
    var context:Context?=null
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val parent:ConstraintLayout = itemView.container
        val city_name:TextView = itemView.city_name_textview
        val weatherDescription: TextView = itemView.city_forecast_textview
        val max_temp: TextView = itemView.max_temperature_textview
        val min_temp: TextView = itemView.min_temperature_textview

        val weatherImage: ImageView = itemView.forceast_imageView
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ForecastAdapter.ForecastViewHolder {

        context = parent.context
        val layoutIdForListItem = R.layout.city_list_item
        val inflater = LayoutInflater.from(context)
        val shouldAttachToParentImmediately = false

        val view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately)

        return ForecastViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.city_name.text = myDataset[position].name
        holder.weatherDescription.text = myDataset[position].weather[0].description
        holder.min_temp.text = myDataset[position].main.temp_min.toString()
        holder.max_temp.text = myDataset[position].main.temp_max.toString()

        Glide.with(context)
            .load("http://openweathermap.org/img/w/${myDataset[position].weather[0].icon}.png")
            .into(holder.weatherImage)
        holder.parent.setOnClickListener{ view: View ->
            Log.d(TAG, myDataset[position].toString())
            Navigation.findNavController(view).navigate(ListFragmentDirections.actionListFragmentToForecastDetailsFragment(myDataset[position].id))}
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}