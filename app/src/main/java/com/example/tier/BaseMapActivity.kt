package com.example.tier

import androidx.appcompat.app.AppCompatActivity

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.Marker
import pub.devrel.easypermissions.EasyPermissions
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.tier.Constant.REQUEST_CODE_LOCATION_PERMISSION
import com.example.tier.base.TierClusterRenderer
import com.example.tier.model.VehicleClusterItem
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import pub.devrel.easypermissions.AppSettingsDialog




abstract class BaseMapActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener, EasyPermissions.PermissionCallbacks {
    lateinit var map: GoogleMap
    lateinit var fuseLocationProviderClient: FusedLocationProviderClient
    lateinit var locationManager: LocationManager
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback
    lateinit var clusterManager: ClusterManager<VehicleClusterItem>
    private var locationMarker: Marker? = null

    override fun onMapReady(googleMap: GoogleMap) {
            map = googleMap
            map.setOnMarkerClickListener(this)
            map.setMinZoomPreference(Constant.MAP_MIN_ZOOM)
            map.setMaxZoomPreference(Constant.MAP_MAX_ZOOM)
            map.uiSettings.isZoomControlsEnabled = true
            updateUserLocationView()
            setCluster()

    }

    override fun onMarkerClick(marker:  Marker): Boolean {
        marker.showInfoWindow()
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }


    override
    fun onPermissionsDenied(requestCode: Int, permissions: List<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, permissions)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {

            //requestLocationPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
     //User gave permission or enabled the gps. Update the location
       updateUserLocationView()
    }


    fun requestLocationPermission(){
            if(requireRelatedPermissions(this)) {
                return
            }
                EasyPermissions.requestPermissions(
                    this,
                    resources.getString(R.string.location_permission_title),
                    REQUEST_CODE_LOCATION_PERMISSION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
    }

    fun createLocationRequest() {
         locationRequest = LocationRequest.create().apply {
             interval = Constant.LOCATION_REQUEST_INTERVAL
             fastestInterval = Constant.LOCATION_REQUEST_FASTEST_INTERVAL
             priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY }

       val builder =  LocationSettingsRequest.Builder()
           .addLocationRequest(locationRequest)
           .setAlwaysShow(true)
        val locationRequestTask = LocationServices.getSettingsClient(this)
            .checkLocationSettings(builder.build())

        locationRequestTask.addOnCompleteListener {task ->
            try {
                val result =  task.getResult(ApiException::class.java)
            } catch (e: ApiException){
                when(e.statusCode){
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        val resolvableApiException = e as ResolvableApiException
                            resolvableApiException.startResolutionForResult(
                            this, Constant.RESOLVABLE_API_EXCEPTION_CODE
                        )
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            Toast.makeText(this, "Settings change not available"
                                ,Toast.LENGTH_LONG).show()

                    }

                }
            }
        }
    }

     private fun setCluster() {
        clusterManager = ClusterManager(applicationContext, map)
        map.setOnCameraIdleListener(clusterManager)
        map.setOnMarkerClickListener(clusterManager)
        clusterManager.renderer = TierClusterRenderer(
            applicationContext,
            map,
            clusterManager
        )
    }

    private fun requireRelatedPermissions(context: Context) =
        EasyPermissions.hasPermissions(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )


    fun updateView(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        val boundsBuilder = LatLngBounds.builder()
        boundsBuilder.include(latLng)
        if(locationMarker == null){
            locationMarker = map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .draggable(false))
        } else
            locationMarker?.let {
            it.position = latLng
        }
        val bounds = boundsBuilder.build()
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10))


    }


     fun updateUserLocationView(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ){
            createLocationRequest()
            fuseLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }

    }
}