package com.bayutb.mystoryapp

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bayutb.mystoryapp.api.SessionManager
import com.bayutb.mystoryapp.data.StoryList
import com.bayutb.mystoryapp.data.StoryMapViewModel
import com.bayutb.mystoryapp.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocation: FusedLocationProviderClient
    private lateinit var viewModel: StoryMapViewModel
    private lateinit var sessionManager: SessionManager

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getLocation()
                }
                permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getLocation()
                }
                else -> {

                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        sessionManager = SessionManager(this)
        viewModel = ViewModelProvider(this)[StoryMapViewModel::class.java]
        viewModel.fetchStoryLocation(sessionManager.checkAuth().toString())
        fusedLocation = LocationServices.getFusedLocationProviderClient(this)



    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true

        viewModel.getMapStory().observe(this){
            if (it != null) {
                addStoryMarker(it)
            }
        }
        getLocation()
    }

    private fun addStoryMarker(stories: List<StoryList>) {
        val boundsBuilder = LatLngBounds.Builder()
        stories.forEach { tourism ->
            val latLng = LatLng(tourism.lat, tourism.lon)
            mMap.addMarker(MarkerOptions().position(latLng).title(tourism.name))
            boundsBuilder.include(latLng)
        }
        val bounds: LatLngBounds = boundsBuilder.build()

        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                30
            )
        )
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getLocation() {
        if (checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)&& checkPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
            fusedLocation.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    showMarker(location)
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun showMarker(location: Location) {
        val startLocation = LatLng(location.latitude, location.longitude)
        mMap.addMarker(
            MarkerOptions().position(startLocation).title(getString(R.string.my_location))
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 15f))
    }

}