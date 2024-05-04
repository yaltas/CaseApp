package com.yusufaltas.caseapp.data.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.yusufaltas.caseapp.data.service.utils.LocationServiceManager
import com.yusufaltas.caseapp.data.service.utils.SharedPreferencesManager
import com.yusufaltas.caseapp.data.service.utils.Constants.LOCATION_NOTIFICATION_CHANNEL_ID
import com.yusufaltas.caseapp.data.service.utils.Constants.LOCATION_NOTIFICATION_ID
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Singleton

@AndroidEntryPoint
@Singleton
class LocationService : Service() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var previousLocation: Location? = null
    private lateinit var sharedPreferencesManager: SharedPreferencesManager


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (LocationServiceManager.isServiceRunning())
            return START_NOT_STICKY

        LocationServiceManager.setServiceRunning(true)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        sharedPreferencesManager = SharedPreferencesManager(this)
        startLocationUpdates()
        startForegroundService()

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        LocationServiceManager.setServiceRunning(false)
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.locations.forEach { location ->
                Log.i("onLocationResult", location.latitude.toString() + " " + location.longitude.toString())
                if (previousLocation != null) {
                    if (sharedPreferencesManager.getLocationList().any()) { //If sharedPreferences is reset while the location service continues to run
                        val distance = previousLocation!!.distanceTo(location)
                        if (distance >= 100) {  //Distance value to be checked
                            saveLocation(location)
                            previousLocation = location
                        }
                    } else {
                        saveLocation(location)
                        previousLocation = location
                    }
                } else {
                    saveLocation(location)
                    previousLocation = location
                }
            }
        }
    }

    private fun startLocationUpdates() {
        //10 second / 5 second
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(5000)
            //.setMaxUpdateDelayMillis(locationMaxWaitTime)
            .build()

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("location", "checkSelfPermission")
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun saveLocation(location: Location) {
        Log.i("saveLocation", location.latitude.toString() + " " + location.longitude.toString())
        sharedPreferencesManager.saveLocation( location.latitude, location.longitude)
    }

    private fun startForegroundService() {
        Log.i("location", "startForegroundService")

        createNotificationChannel()
        val notification = NotificationCompat.Builder(this, LOCATION_NOTIFICATION_CHANNEL_ID)
            .setContentText("CaseApp Location Service")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        startForeground(LOCATION_NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                LOCATION_NOTIFICATION_CHANNEL_ID,
                "Location Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }
}