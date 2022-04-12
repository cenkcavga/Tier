package com.example.tier.network

import com.example.tier.Constant
import com.example.tier.model.VehicleResponse
import retrofit2.http.GET
import retrofit2.Response

interface ApiService {
    @GET(Constant.END_POINT)
    suspend fun getVehiclesOnLocation(): Response<VehicleResponse>
}