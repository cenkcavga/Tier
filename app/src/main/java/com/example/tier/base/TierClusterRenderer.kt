package com.example.tier.base


import android.content.Context
import com.example.tier.model.VehicleClusterItem
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer


// Custom cluster to make group of close vehicles
class TierClusterRenderer(
    context: Context?, map: GoogleMap?,
    clusterManager: ClusterManager<VehicleClusterItem>
) :
    DefaultClusterRenderer<VehicleClusterItem>(context, map, clusterManager) {
    override fun onBeforeClusterItemRendered(item: VehicleClusterItem, markerOptions: MarkerOptions) {
        markerOptions.icon(item.getMarkerOptions().icon)
        markerOptions.snippet(item.snippet)
        markerOptions.title(item.title)
        super.onBeforeClusterItemRendered(item, markerOptions)
    }
}