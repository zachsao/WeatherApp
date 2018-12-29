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

class ForecastAdapter(private val myDataset: List<City>) :
    RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val parent = itemView.findViewById<ConstraintLayout>(R.id.container)
        val city_name = itemView.findViewById<TextView>(R.id.city_name_textview)

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
        holder.parent.setOnClickListener{ view: View ->
            Navigation.findNavController(view).navigate(R.id.action_listFragment_to_forecastDetailsFragment)}
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}