package com.example.tier.repository

import android.content.res.Resources
import com.example.tier.R
import com.example.tier.model.Vehicle
import com.example.tier.model.VehicleResponse
import com.example.tier.network.ApiService
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class VehicleRepository @Inject constructor (
    @Named("ApiService") private val apiService: ApiService,
    private val resources: Resources) {

    suspend fun getVehiclesOnLocation(): Response<VehicleResponse> {
        val response = apiService.getVehiclesOnLocation()
        response.body().let {
            return if(it?.data.isNullOrEmpty())
                response
            else {
                val vehicles = createDialogTitles(it!!.data )
                response.body()?.data = vehicles
                response
            }
        }
    }

    private fun createDialogTitles(vehicles: List<Vehicle>): List<Vehicle> {
            for (vehicle in vehicles) {
                vehicle.dialogTitle = "${"%"}${vehicle.attributes.batteryLevel} " +
                    "${resources.getString(R.string.charge)} " +
                    getHelmetStatus(vehicle.attributes.hasHelmetBox)
        }

        return vehicles
    }

    private fun getHelmetStatus(hasHelmetBox: Boolean): String {
        return if(hasHelmetBox){
            resources.
            getString(R.string.helmet_available)
        } else
            resources.getString(R.string.helmet_not_available)
        }

}