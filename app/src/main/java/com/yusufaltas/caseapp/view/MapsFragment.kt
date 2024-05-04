package com.yusufaltas.caseapp.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.yusufaltas.caseapp.R
import com.yusufaltas.caseapp.data.service.LocationService
import com.yusufaltas.caseapp.data.service.utils.viewBinding
import com.yusufaltas.caseapp.databinding.MapsLayoutBinding
import com.yusufaltas.caseapp.utils.DialogHelper.showDialog
import com.yusufaltas.caseapp.viewmodel.MapsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsFragment : Fragment(R.layout.maps_layout), OnMapReadyCallback {
    private val binding by viewBinding(MapsLayoutBinding::bind)
    private val mapsViewModel: MapsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapsViewModel.registerOnSharedPreferenceChangeListener(requireContext())
        initMap()

        with(binding) {
            btnStartService.setOnClickListener {
                startLocationTracking()
            }
            btnStopService.setOnClickListener {
                stopLocationTracking()
            }

            btnClearLocation.setOnClickListener {
                mapsViewModel.getSharedPreferences().clearLocationList()
            }

            btnListLastLocations.setOnClickListener { //SharedPref. last info list
                val lastLocation = mapsViewModel.getSharedPreferences().getLocationList()
                for (location in lastLocation) {
                    Log.d("Location", "Latitude: ${location.first}, Longitude: ${location.second}")
                }
            }
        }

    }

    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            mapsViewModel.setMapInstance(googleMap)
            onMapReady(googleMap)
        }

    }

    override fun onDestroyView() {
        mapsViewModel.unregisterOnSharedPreferenceChangeListener(requireContext())
        super.onDestroyView()
    }


    //region LOCATION TRACKING SERVICE
    private fun startLocationTracking() {
        Log.i("location", "startLocationTracking")
        mapsViewModel.getSharedPreferences().clearLocationList()
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
        googleMap.let { gMap ->
            mapsViewModel.setMapInstance(gMap)
            mapsViewModel.addMarkers()
            mapsViewModel.map.setOnMarkerClickListener { marker ->
                val position = marker.tag as LatLng
                val address = mapsViewModel.getAddressFromLocation(requireContext(), position)
                address?.let {
                    showDialog(requireContext(), getString(R.string.address_info), it)
                }
                true
            }
            mapsViewModel.moveCameraToLastLocation()
        }
    }
}