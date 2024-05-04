package com.yusufaltas.caseapp.model

import android.Manifest

object Constants {
    const val BASE_URL = "https://marti.tech/api/"

    //SHARED PREFERENCES
    const val SHARED_PREFERENCES_NAME = "LocationPreferences"
    const val KEY_LATITUDE_LIST = "latitude_list"
    const val KEY_LONGITUDE_LIST = "longitude_list"

    //NOTIFICATIONS
    const val LOCATION_NOTIFICATION_CHANNEL_ID = "location_service_channel"
    const val LOCATION_NOTIFICATION_ID = 1

    //TIMEOUTS
    const val CONNECT_TIMEOUT = 200L
    const val READ_TIMEOUT = 200L
    const val WRITE_TIMEOUT = 200L

    //PERMISSIONS
    val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        //Manifest.permission.ACCESS_BACKGROUND_LOCATION
    )
}