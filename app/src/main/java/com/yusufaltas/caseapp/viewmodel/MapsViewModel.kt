package com.yusufaltas.caseapp.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.yusufaltas.caseapp.data.repository.main.CaseRepository
import com.yusufaltas.caseapp.data.service.utils.SharedPreferencesManager
import com.yusufaltas.caseapp.model.Constants.KEY_LATITUDE_LIST
import com.yusufaltas.caseapp.model.Constants.KEY_LONGITUDE_LIST
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val apiRepository: CaseRepository, private val sharedPreferencesManager: SharedPreferencesManager) : ViewModel(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    lateinit var map: GoogleMap
    //Burada apiRepository kullanımı yapılmadı, apiRepository kullanım örneği olarak bırakıldı.
    fun setMapInstance(googleMap: GoogleMap) {
        map = googleMap
    }

    fun getSharedPreferences() = sharedPreferencesManager

    fun registerOnSharedPreferenceChangeListener(context: Context) {
        sharedPreferencesManager.getSharedPreferences(context).registerOnSharedPreferenceChangeListener(this)
    }

    fun unregisterOnSharedPreferenceChangeListener(context: Context) {
        sharedPreferencesManager.getSharedPreferences(context).unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == KEY_LATITUDE_LIST || key == KEY_LONGITUDE_LIST)  // SharedPreferences değişikliği işlemleri burada gerçekleştirilebilir
            refreshMap()
    }

    fun addMarkers() {
        for (location in sharedPreferencesManager.getLocationList()) {
            val latLng = LatLng(location.first, location.second)
            val marker = map.addMarker(MarkerOptions().position(latLng))
            marker?.tag = latLng // İşaretleyicinin etiket özelliği adres bilgisini ekler
        }
    }

    fun getAddressFromLocation(context: Context, latLng: LatLng): String? {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        return addresses?.firstOrNull()?.getAddressLine(0)
    }

    fun moveCameraToLastLocation() {
        val locationList = sharedPreferencesManager.getLocationList()
        if (locationList.isNotEmpty()) {
            val lastLocation = locationList.last()
            val latLng = LatLng(lastLocation.first, lastLocation.second)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f))
        }
    }

    private fun refreshMap() {
        val locationList = sharedPreferencesManager.getLocationList()
        map.clear()
        //Update map based on new location data
        for (location in locationList) {
            val latLng = LatLng(location.first, location.second)
            val marker = map.addMarker(MarkerOptions().position(latLng))
            marker?.tag = latLng //Marker's label feature adds address information
        }

        if (locationList.isNotEmpty()) {
            val lastLocation = locationList.last()
            val latLng = LatLng(lastLocation.first, lastLocation.second)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f))

        }
    }
}