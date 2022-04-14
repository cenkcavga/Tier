package com.example.tier.repository

import android.content.res.Resources
import com.example.tier.R
import com.example.tier.base.VehicleRepositoryContract
import com.example.tier.model.Vehicle
import com.example.tier.model.VehicleResponse
import com.example.tier.network.ApiService
import com.example.tier.network.NetworkResult
import javax.inject.Inject
import javax.inject.Named

class VehicleRepository @Inject constructor (
    @Named("ApiService") private val apiService: ApiService,
    private val resources: Resources)  : VehicleRepositoryContract {

    /*
      Api call for list of vehicles
     */
    override suspend fun getVehiclesOnLocation(): NetworkResult<VehicleResponse> {
        val response = apiService.getVehiclesOnLocation()
        response.let {networkResult ->
            when(networkResult) {
                //Network operation success....
                is NetworkResult.Success -> {
                    networkResult.data.let { vehiclesResponse ->
                        val vehiclesList = createDialogTitles(vehicles = vehiclesResponse!!.data)
                        networkResult.data!!.data = vehiclesList
                    }
                }
                is NetworkResult.Failure -> {
                    networkResult.exception
                }
            }
        }
        return response
    }


    /*
      Creating title string from attribute parameters for each of vehicles
     */

    private fun createDialogTitles(vehicles: MutableList<Vehicle>): MutableList<Vehicle> {
            for (vehicle in vehicles) {
                vehicle.info = "${"%"}${vehicle.attributes.batteryLevel} " +
                    "${resources.getString(R.string.charge)} " +
                    getHelmetStatus(vehicle.attributes.hasHelmetBox)
        }

        return vehicles
    }


    /*
       Returns vehicle helmet availability string
     */

    private fun getHelmetStatus(hasHelmetBox: Boolean): String {
        return if(hasHelmetBox){
            resources.
            getString(R.string.helmet_available)
        } else
            resources.getString(R.string.helmet_not_available)
        }

}