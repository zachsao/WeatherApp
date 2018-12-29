package com.example.zach.weatherapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.zach.weatherapp.R
import com.example.zach.weatherapp.data.City
import kotlinx.android.synthetic.main.city_list_item.view.*

class ForecastAdapter(var myDataset: List<City>) :
    RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val parent = itemView.container
        val city_name = itemView.city_name_textview
        val weatherDescription = itemView.city_forecast_textview
        val max_temp = itemView.max_temperature_textview
        val min_temp = itemView.min_temperature_textview
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ForecastAdapter.ForecastViewHolder {
        val context = parent.context
        val layoutIdForListItem = R.layout.city_list_item
        val inflater = LayoutInflater.from(context)
        val shouldAttachToParentImmediately = false

        val view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately)

        return ForecastViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.city_name.text = myDataset.get(position).name
        holder.weatherDescription.text = myDataset.get(position).weather[0].description
        holder.min_temp.text = myDataset.get(position).main.temp_min.toString()
        holder.max_temp.text = myDataset.get(position).main.temp_max.toString()

        holder.parent.setOnClickListener{ view: View ->
            Navigation.findNavController(view).navigate(R.id.action_listFragment_to_forecastDetailsFragment)}
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}