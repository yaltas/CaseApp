package com.yusufaltas.caseapp.data.service.utils

import android.content.Context
import android.content.SharedPreferences
import com.yusufaltas.caseapp.data.service.utils.Constants.KEY_LATITUDE_LIST
import com.yusufaltas.caseapp.data.service.utils.Constants.KEY_LONGITUDE_LIST
import com.yusufaltas.caseapp.data.service.utils.Constants.SHARED_PREFERENCES_NAME
import javax.inject.Inject

class SharedPreferencesManager @Inject constructor(private val context: Context) {

    fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun saveLocation(latitude: Double, longitude: Double) {
        val sharedPreferences = getSharedPreferences(context)
        val latitudeList = sharedPreferences.getStringSet(KEY_LATITUDE_LIST, mutableSetOf())?.toMutableSet()
        val longitudeList = sharedPreferences.getStringSet(KEY_LONGITUDE_LIST, mutableSetOf())?.toMutableSet()

        latitudeList?.add(latitude.toString())
        longitudeList?.add(longitude.toString())

        sharedPreferences.edit().apply {
            putStringSet(KEY_LATITUDE_LIST, latitudeList)
            putStringSet(KEY_LONGITUDE_LIST, longitudeList)
            apply()
        }
    }

    fun getLocationList(): List<Pair<Double, Double>> {
        val sharedPreferences = getSharedPreferences(context)
        val latitudeList = sharedPreferences.getStringSet(KEY_LATITUDE_LIST, setOf()) ?: setOf()
        val longitudeList = sharedPreferences.getStringSet(KEY_LONGITUDE_LIST, setOf()) ?: setOf()

        return latitudeList.zip(longitudeList).map { Pair(it.first.toDouble(), it.second.toDouble()) }
    }

    fun clearLocationList() {
        val sharedPreferences = getSharedPreferences(context)
        sharedPreferences.edit().remove(KEY_LATITUDE_LIST).remove(KEY_LONGITUDE_LIST).apply()
    }
}