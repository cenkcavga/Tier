package com.example.tier

import androidx.appcompat.app.AppCompatActivity
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

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    private  val vehicleMapViewModel: VehicleMapViewModel by viewModels()
    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var vehicles: List<Vehicle>
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.map.getFragment<SupportMapFragment>().getMapAsync(this)
        vehicleMapViewModel.liveData.observe(this){
            vehicles = it
            addVehiclesOnMap(vehicles)
        }
    }

    override fun onMapReady(LocalMap: GoogleMap) {
        map = LocalMap
        map.setOnMarkerClickListener(this)

    }
     private fun addVehiclesOnMap(vehicles: List<Vehicle>){
         val boundsBuilder= LatLngBounds.builder()
         for (vehicle in vehicles){
             val position = LatLng(vehicle.attributes.lat,vehicle.attributes.lng)
             map.addMarker(getMarkerOptions(vehicle.dialogTitle, position, vehicle.attributes))
             boundsBuilder.include(position)
         }
         val bounds= boundsBuilder.build()
         map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
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

    override fun onMarkerClick(marker: Marker): Boolean {
            marker.showInfoWindow()
        return true
    }
}