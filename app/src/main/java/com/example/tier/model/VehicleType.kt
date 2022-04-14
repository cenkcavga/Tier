package com.example.tier.model

sealed class VehicleType(val vehicleType: String) {
    object  SCOOTER: VehicleType("escooter")
    object  BIKE:    VehicleType("ebicycle")
    object  MOPED:   VehicleType("emoped")
}