package com.example.tier.base

import com.example.tier.model.VehicleResponse
import com.example.tier.network.NetworkResult

interface VehicleRepositoryContract {
    suspend fun getVehiclesOnLocation(): NetworkResult<VehicleResponse>
}