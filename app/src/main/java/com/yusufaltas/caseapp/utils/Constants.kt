package com.yusufaltas.caseapp.utils

import android.Manifest

object Constants {
    const val BASE_URL = "https://marti.com/api/"

    //TIMEOUTS
    const val CONNECT_TIMEOUT = 200L
    const val READ_TIMEOUT = 200L
    const val WRITE_TIMEOUT = 200L

    //PERMISSIONS
    val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
//        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    )
}