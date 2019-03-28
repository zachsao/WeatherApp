package com.example.zach.weatherapp.utils

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Task
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient


private const val LOCATION_REQUEST_INTERVAL: Long = 40000
private const val LOCATION_REQUEST_FASTEST_INTERVAL: Long = 20000
private const val LOCATION_REQUEST_PRIORITY: Int = LocationRequest.PRIORITY_HIGH_ACCURACY

@Singleton
open class LocationService @Inject constructor(private val app: Application) {
    private val locationRequest = LocationRequest().apply {
        interval = LOCATION_REQUEST_INTERVAL
        fastestInterval = LOCATION_REQUEST_FASTEST_INTERVAL
        priority = LOCATION_REQUEST_PRIORITY
    }
    private val locationsSubject = BehaviorSubject.create<Location>()
    private val fusedLocationClient = FusedLocationProviderClient(app)
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.let { locationsSubject.onNext(locationResult.lastLocation) }
        }
    }

    private var isLocationUpdateRunning = false

    open fun registerToLocations(): Observable<Location> {
        return locationsSubject
    }

    open fun stopLocationUpdates(): Completable {
        return Completable.fromAction {
            Timber.d("Stopping location updates")
            fusedLocationClient.removeLocationUpdates(locationCallback)
            isLocationUpdateRunning = false
            Timber.d("Location updates stopped")
        }
    }

    open fun startLocationUpdates(): Completable {
        return Single.just(isLocationUpdateRunning)
            .filter { alreadyRunning -> return@filter !alreadyRunning }
            .flatMapCompletable { checkLocationPermission() }
            .andThen(checkLocationSettings())
            .andThen(requestLocationUpdates())
    }

    private fun checkLocationPermission(): Completable {
        return Completable.fromAction {
            if (ContextCompat.checkSelfPermission(
                    app,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                throw SecurityException("Permissions not granted")
            }
        }
    }

    private fun checkLocationSettings(): Completable {
        return Completable.create { emitter ->
            Timber.d("Checking Location Settings")
            val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
            val client: SettingsClient = LocationServices.getSettingsClient(app)
            val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
            task.addOnFailureListener { exception ->
                emitter.onError(exception)
            }
            task.addOnSuccessListener {
                emitter.onComplete()
            }
        }
    }

    private fun requestLocationUpdates(): Completable {
        return Completable.fromAction {
            Timber.d("Starting location updates")
            try {
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
                isLocationUpdateRunning = true
                Timber.d("Location updates started")
            } catch (securityException: SecurityException) {
                isLocationUpdateRunning = false
                Timber.e(securityException)
                throw securityException
            }
        }
    }
}
