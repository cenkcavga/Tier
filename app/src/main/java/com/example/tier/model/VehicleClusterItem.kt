package com.example.tier.model

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterItem

class VehicleClusterItem(
    markerOption: MarkerOptions,
    vehicle: Vehicle
) : ClusterItem {
    private val markerOption: MarkerOptions = markerOption
    private val position: LatLng = LatLng(vehicle.attributes.lat, vehicle.attributes.lng)
    private val title: String = vehicle.dialogTitle ?: ""
    private val snippet: String = vehicle.attributes.vehicleType
    fun getMarkerOptions(): MarkerOptions {
        return markerOption
    }
    override fun getPosition(): LatLng {
        return position
    }
    override fun getTitle(): String? {
        return title
    }
    override fun getSnippet(): String? {
        return snippet
    }
}