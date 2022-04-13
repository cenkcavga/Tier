package com.example.tier.model

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterItem
import javax.inject.Named

class VehicleClusterItem(
    markerOption: MarkerOptions
) : ClusterItem {
    private val markerOption: MarkerOptions = markerOption
    private val position: LatLng = markerOption.position
    private val snippet: String = markerOption.snippet.toString()
    private val title: String = markerOption.title.toString()
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