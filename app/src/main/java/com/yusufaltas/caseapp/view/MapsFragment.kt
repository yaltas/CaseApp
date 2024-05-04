package com.yusufaltas.caseapp.view

import android.content.Intent
import android.content.SharedPreferences
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.yusufaltas.caseapp.R
import com.yusufaltas.caseapp.data.service.LocationService
import com.yusufaltas.caseapp.data.service.utils.SharedPreferencesManager
import com.yusufaltas.caseapp.data.service.utils.viewBinding
import com.yusufaltas.caseapp.databinding.MapsLayoutBinding
import com.yusufaltas.caseapp.utils.Constants.KEY_LATITUDE_LIST
import com.yusufaltas.caseapp.utils.Constants.KEY_LONGITUDE_LIST
import com.yusufaltas.caseapp.utils.DialogHelper.showDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MapsFragment : Fragment(R.layout.maps_layout), OnMapReadyCallback, SharedPreferences.OnSharedPreferenceChangeListener {
    private val binding by viewBinding(MapsLayoutBinding::bind)
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var map: GoogleMap
    private lateinit var geocoder: Geocoder


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferencesManager = SharedPreferencesManager(requireContext())
        sharedPreferencesManager.getSharedPreferences(requireContext()).registerOnSharedPreferenceChangeListener(this)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        geocoder = Geocoder(requireContext(), Locale.getDefault())

        with(binding) {
            imgFilterView.setOnClickListener {
                startLocationTracking()
            }
            imgSearch.setOnClickListener {
                stopLocationTracking()
            }
            imgListView.setOnClickListener { //SharedPref. last info list
                val lastLocation = sharedPreferencesManager.getLocationList(requireContext())
                for (location in lastLocation) {
                    Log.d("Location", "Latitude: ${location.first}, Longitude: ${location.second}")
                }
            }
        }

    }

    override fun onDestroyView() {
        sharedPreferencesManager.getSharedPreferences(requireContext()).unregisterOnSharedPreferenceChangeListener(this)
        super.onDestroyView()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        // Refreshing the map when the location list changes
        if (key == KEY_LATITUDE_LIST || key == KEY_LONGITUDE_LIST)
            refreshMap()
    }

    //region LOCATION TRACKING SERVICE
    private fun startLocationTracking() {
        Log.i("location", "startLocationTracking")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            requireActivity().startForegroundService(Intent(requireContext(), LocationService::class.java))
        else
            requireActivity().startService(Intent(requireContext(), LocationService::class.java))
    }

    private fun stopLocationTracking() {
        Log.i("location", "stopLocationTracking")
        requireActivity().stopService(Intent(requireContext(), LocationService::class.java))
    }
    //endregion LOCATION TRACKING SERVICE

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.let {
            map = it
            val locationList = sharedPreferencesManager.getLocationList(requireContext())
            for (location in locationList) {
                val latLng = LatLng(location.first, location.second)
                val marker = map.addMarker(MarkerOptions().position(latLng))
                marker?.tag = latLng // Marker'ın tag özelliğine konum bilgisini ekleyin
            }
            map.setOnMarkerClickListener { marker ->
                val position = marker.tag as LatLng
                val addresses = geocoder.getFromLocation(position.latitude, position.longitude, 1)
                val address = addresses?.firstOrNull()?.getAddressLine(0)
                address?.let {
                    showDialog(requireContext(), getString(R.string.address_info), it)
                }
                true
            }
            if (locationList.isNotEmpty()) {
                val lastLocation = locationList.last()
                val latLng = LatLng(lastLocation.first, lastLocation.second)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f))
            }
        }

    }

    private fun refreshMap() {
        val locationList = sharedPreferencesManager.getLocationList(requireContext())
        map.clear() // Remove markers from map

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