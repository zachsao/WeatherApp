package com.example.zach.weatherapp.ui

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zach.weatherapp.BuildConfig
import com.example.zach.weatherapp.R
import com.example.zach.weatherapp.adapter.CityAdapter
import com.example.zach.weatherapp.databinding.FragmentListBinding
import com.example.zach.weatherapp.utils.Injectable
import com.example.zach.weatherapp.viewModel.CityListViewModel
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import timber.log.Timber
import javax.inject.Inject

class ListFragment : Fragment(), Injectable {

    companion object {
        const val MY_PERMISSIONS_REQUEST_ACCESS_LOCATION = 1
        const val REQUEST_CODE_AUTOCOMPLETE = 2
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var progressView: ProgressBar
    lateinit var viewModel: CityListViewModel
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var binding: FragmentListBinding

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (!hasLocationPermission())
            requestLocationPermission()
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)
        viewManager = LinearLayoutManager(activity)
        recyclerView = binding.list
        progressView = binding.progressBar

        viewModel = ViewModelProviders.of(this, factory).get(CityListViewModel::class.java)

        if (isOnline()) {
            viewModel.listenToLocations()
            displayList()
        } else {
            binding.emptyStateTextView.visibility = View.VISIBLE
            binding.listFragment.visibility = View.GONE
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun displayList() {
        binding.emptyStateTextView.visibility = View.GONE
        showProgress(true)

        viewModel.getCities().observe(this, Observer { cities ->
            Timber.i("Observing cities : %s", cities.toString())
            viewAdapter = CityAdapter(cities)
            recyclerView.apply {
                // use a linear layout manager
                layoutManager = viewManager
                // specify a viewAdapter
                adapter = viewAdapter
            }
            showProgress(false)
        })
    }

    private fun isOnline(): Boolean {
        val connMgr = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo
        return networkInfo?.isConnected == true
    }

    private fun showProgress(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime)

        recyclerView.visibility = if (show) View.GONE else View.VISIBLE
        recyclerView.animate().setDuration(shortAnimTime.toLong()).alpha(
            (if (show) 0 else 1).toFloat()
        ).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                recyclerView.visibility = if (show) View.GONE else View.VISIBLE
            }
        })

        progressView.visibility = if (show) View.VISIBLE else View.GONE
        progressView.animate().setDuration(shortAnimTime.toLong()).alpha(
            (if (show) 1 else 0).toFloat()
        ).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                progressView.visibility = if (show) View.VISIBLE else View.GONE
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            val feature = PlaceAutocomplete.getPlace(data)
            viewModel.getCityByName(feature.placeName()?: "Paris").observe(this@ListFragment, Observer { city ->
                findNavController(this).navigate(ListFragmentDirections.actionListFragmentToForecastDetailsFragment(city.id))
            })
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.refresh -> {
                if (isOnline()) {
                    viewModel.listenToLocations()
                }
                true
            }
            R.id.search -> openAutoComplete()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openAutoComplete(): Boolean {
        val placeOptions = PlaceOptions.builder()
            .hint("Search a city")
            .backgroundColor(Color.WHITE)
            .geocodingTypes("region", "place") // We only want to search a city
            .historyCount(0)
            .build()

        val intent = PlaceAutocomplete.IntentBuilder()
            .accessToken(BuildConfig.MAPBOX_AUTOCOMPLETE_API_KEY)
            .placeOptions(placeOptions)
            .build(activity)
        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE)
        return true
    }

    override fun onResume() {
        super.onResume()
        viewModel.startLocation()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopLocation()
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity!!, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            MY_PERMISSIONS_REQUEST_ACCESS_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_ACCESS_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! update the location
                    Timber.i("permission granted !")
                    viewModel.startLocation()
                    viewModel.listenToLocations()
                } else {
                    // permission denied, boo!
                    Timber.i("permission denied !")
                    Toast.makeText(activity, "To continue, enable permission in settings", LENGTH_SHORT).show()
                }
                return
            }
        }
    }

}
