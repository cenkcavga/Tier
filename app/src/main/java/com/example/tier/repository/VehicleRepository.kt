package com.example.tier.repository

import com.example.tier.model.VehicleResponse
import com.example.tier.network.ApiService
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class VehicleRepository @Inject constructor (
    @Named("ApiService") private val apiService: ApiService ) {

    suspend fun getVehiclesOnLocation(): Response<VehicleResponse> {
        return apiService.getVehiclesOnLocation()
    }
}