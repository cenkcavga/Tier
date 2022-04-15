package com.example.tier.repository

import android.content.res.Resources
import com.example.tier.base.VehicleRepositoryContract
import com.example.tier.model.Attributes
import com.example.tier.model.Vehicle
import com.example.tier.model.VehicleResponse
import com.example.tier.network.NetworkResult

class FakeVehicleRepository :VehicleRepositoryContract{

    private var isNetworkResultSuccess = false

    fun setShouldReturnSuccess(value: Boolean) {
        isNetworkResultSuccess = value
    }

    override
    suspend fun getVehiclesOnLocation(): NetworkResult<VehicleResponse> {
       return if(isNetworkResultSuccess)
           getFakeSuccessResponse()
        else
           getFakeFailExceptionResponse()


    }



    private fun getFakeSuccessResponse(): NetworkResult<VehicleResponse>{
        val vehicles: MutableList<Vehicle> = mutableListOf()
        vehicles.add(
            Vehicle (
                type= "vehicle",
                id= "65743322",
                info = null,
                attributes = Attributes(
                    batteryLevel	= 90,
                    lat	=	52.33434,
                    lng	=	13.24645,
                    maxSpeed	=	40,
                    vehicleType = "emoped",
                    hasHelmetBox = true
                )
            )
        )

        vehicles.add(
            Vehicle (
                type= "vehicle",
                id= "324237",
                info = null,
                attributes = Attributes(
                    batteryLevel	= 40,
                    lat	=	52.34434,
                    lng	=	13.4467,
                    maxSpeed	=	20,
                    vehicleType = "escoter",
                    hasHelmetBox = true
                )
            )
        )

        vehicles.add(
            Vehicle (
                type= "vehicle",
                id= "3487593",
                info = null,
                attributes = Attributes(
                    batteryLevel	= 30,
                    lat	=	52.34434,
                    lng	=	13.4467,
                    maxSpeed	=	40,
                    vehicleType = "ebicycle",
                    hasHelmetBox = false
                )
            )
        )

        // Assigns info parameter from attributes
       val vehicleList = createDialogTitles(vehicles)
       return  NetworkResult.Success(VehicleResponse(vehicleList))

    }




    /*
      Creating title string from attribute parameters for each of vehicles
     */

    private fun createDialogTitles(vehicles: MutableList<Vehicle>): MutableList<Vehicle> {
        for (vehicle in vehicles) {
            vehicle.info = "${"%"}${vehicle.attributes.batteryLevel} " +
                "charge " +
                getHelmetStatus(vehicle.attributes.hasHelmetBox)
        }

        return vehicles
    }


    /*
       Returns vehicle helmet availability string
     */

    private fun getHelmetStatus(hasHelmetBox: Boolean): String {
        return if(hasHelmetBox){
            "Helmet available"
        } else
            "Helmet is not available"
    }



    private fun getFakeFailExceptionResponse(): NetworkResult<VehicleResponse> {
        return NetworkResult.Failure(
            404, Resources.NotFoundException()
        )
    }









}