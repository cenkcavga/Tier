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
import com.example.tier.model.VehicleClusterItem
import com.example.tier.network.NetworkResult
import com.google.android.gms.location.*
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.example.tier.base.BaseMapActivity

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
            when(it){
                is NetworkResult.Success -> {
                    vehicles = it.data!!.data
                    addVehiclesOnMap(vehicles)
                }

                // Handling failure based on exceptions
                is NetworkResult.Failure -> {
                    when(it.statusCode){
                        Constant.NO_INTERNET ->
                            showDialog(resources.getString(R.string.no_internet))

                            Constant.NOT_FOUND ->
                                showDialog(resources.getString(R.string.api_error))

                            Constant.UNAUTHORIZED ->
                                showDialog(resources.getString(R.string.api_error))
                   }
                }
            }

        }
    }



    /*
         Adding each of vehicles on right position on the map
         as marker with cluster groups.
     */

    private fun addVehiclesOnMap(vehicles: List<Vehicle>){
         for (vehicle in vehicles){
             val position = LatLng(vehicle.attributes.lat,vehicle.attributes.lng)
             clusterManager.addItem(
                 VehicleClusterItem(
                     markerOption = getMarkerOptions(
                         title = vehicle.info,
                         snippet= vehicle.attributes.vehicleType,
                         position = position,
                         attributes = vehicle.attributes
                     ),
                 ))

         }

     }



    /*
            Returns marker option model for each of vehicles
     */

    private fun getMarkerOptions(title:String?, position: LatLng, attributes: Attributes
                                 , snippet: String): MarkerOptions {
        val markerOptions = MarkerOptions()
            .snippet(snippet)
            .position(position)
            .title(title)
            .draggable(false)

        return when(attributes.vehicleType ) {
            VehicleType.SCOOTER.vehicleType ->
               markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.scooter))

            VehicleType.BIKE.vehicleType ->
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.bike))

            VehicleType.MOPED.vehicleType ->
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.moped))

            else ->
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.questionmark))
        }
    }



    private fun showDialog(message: String) {
        val dialogClickListener =
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        dialog.dismiss()
                        vehicleMapViewModel.getVehiclesOnMap()
                    }

                    DialogInterface.BUTTON_NEGATIVE -> {
                        dialog.dismiss()
                        finish()
                    }
                }
            }
          val builder =AlertDialog.Builder(this)
          builder.setMessage(message)
            .setPositiveButton(resources.getString(R.string.try_again), dialogClickListener)
            .setNegativeButton(resources.getString(R.string.no), dialogClickListener)
            .show()

    }




}