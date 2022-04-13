package com.example.tier

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.example.tier.databinding.ActivityMainBinding
import com.example.tier.model.Attributes
import com.example.tier.model.Vehicle
import com.example.tier.model.VehicleType
import com.example.tier.viewmodel.VehicleMapViewModel
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.tasks.Task

import android.content.IntentSender
import android.content.IntentSender.SendIntentException
import android.os.Looper

import com.google.android.gms.common.api.ResolvableApiException

import com.google.android.gms.common.api.ApiException

import androidx.annotation.NonNull
import com.example.tier.base.TierClusterRenderer
import com.example.tier.model.VehicleClusterItem
import com.google.android.gms.location.*

import com.google.android.gms.tasks.OnCompleteListener
import com.google.maps.android.clustering.ClusterManager
import java.lang.ClassCastException
import kotlin.let as let

@AndroidEntryPoint
class MainActivity : BaseMapActivity(){

    private  val vehicleMapViewModel: VehicleMapViewModel by viewModels()
    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var vehicles: List<Vehicle>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.map.getFragment<SupportMapFragment>().getMapAsync(this)
        requestLocationPermission()
        fuseLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationManager =  getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
               locationResult.let { result ->
                   result?.let { updateView(it.lastLocation) }
               }

            }
        }
        getUserLocation()
        vehicleMapViewModel.liveData.observe(this){
            vehicles = it
            addVehiclesOnMap(vehicles)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.v("PermissionsResult","onRequestPermissionsResult")
        when (requestCode) {
            Constant.REQUEST_CODE_LOCATION_PERMISSION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    updateLocation()
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    finish()
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }


    private fun addVehiclesOnMap(vehicles: List<Vehicle>){
         for (vehicle in vehicles){
             val position = LatLng(vehicle.attributes.lat,vehicle.attributes.lng)
             //map.addMarker(getMarkerOptions(vehicle.dialogTitle, position, vehicle.attributes))
             clusterManager.addItem(
                 VehicleClusterItem(
                     markerOption = getMarkerOptions(
                         vehicle.dialogTitle,
                         position,
                         vehicle.attributes
                     ),
                     vehicle = vehicle
                 ))

         }

     }



    private fun getMarkerOptions(title:String?, position: LatLng, attributes: Attributes): MarkerOptions {
        return when(attributes.vehicleType ) {
            VehicleType.E_SCOOTER.vehicleType ->
                MarkerOptions()
                    .position(position)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.scooter))
                    .title(title)
                    .draggable(false)
            VehicleType.E_BIKE.vehicleType ->
                MarkerOptions()
                    .position(position)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bike))
                    .title(title)
                    .draggable(false)
            VehicleType.E_MOPED.vehicleType ->
                MarkerOptions()
                    .position(position)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.moped))
                    .title(title)
                    .draggable(false)

            else ->
                MarkerOptions()
                    .position(position)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.questionmark))
                    .title(title)
                    .draggable(false)
        }
    }

    private fun getUserLocation(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
                if(isGpsEnabled()) {
                    fuseLocationProviderClient.lastLocation
                        .addOnSuccessListener(this) { location ->
                            updateView(location = location)

                        }
                }
                else
                    createLocationRequest()
    }




   private fun updateLocation(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
        fuseLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }


    private fun updateView(location: Location){
        val latLng = LatLng(location.latitude, location.longitude)
        val boundsBuilder = LatLngBounds.builder()
        boundsBuilder.include(latLng)
        val bounds = boundsBuilder.build()
        map.addMarker(
            MarkerOptions()
                .position(latLng)
                .draggable(false)
        )
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10))
    }


}