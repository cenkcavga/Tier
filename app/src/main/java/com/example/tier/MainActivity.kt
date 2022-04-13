package com.example.tier

import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.viewModels
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
                   result?.let {
                       updateView(it.lastLocation)
                   }
               }

            }
        }
        vehicleMapViewModel.liveData.observe(this){
            vehicles = it
            addVehiclesOnMap(vehicles)
        }
    }


    private fun addVehiclesOnMap(vehicles: List<Vehicle>){
         for (vehicle in vehicles){
             val position = LatLng(vehicle.attributes.lat,vehicle.attributes.lng)
             clusterManager.addItem(
                 VehicleClusterItem(
                     markerOption = getMarkerOptions(
                         title = vehicle.dialogTitle,
                         snippet= vehicle.attributes.vehicleType.toString(),
                         position = position,
                         attributes = vehicle.attributes
                     ),
                 ))

         }

     }



    private fun getMarkerOptions(title:String?, position: LatLng, attributes: Attributes, snippet: String): MarkerOptions {
        return when(attributes.vehicleType ) {
            VehicleType.E_SCOOTER.vehicleType ->
                MarkerOptions()
                    .snippet(snippet)
                    .position(position)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.scooter))
                    .title(title)
                    .draggable(false)
            VehicleType.E_BIKE.vehicleType ->
                MarkerOptions()
                    .snippet(snippet)
                    .position(position)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bike))
                    .title(title)
                    .draggable(false)
            VehicleType.E_MOPED.vehicleType ->
                MarkerOptions()
                    .snippet(snippet)
                    .position(position)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.moped))
                    .title(title)
                    .draggable(false)

            else ->
                MarkerOptions()
                    .snippet(snippet)
                    .position(position)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.questionmark))
                    .title(title)
                    .draggable(false)
        }
    }




}