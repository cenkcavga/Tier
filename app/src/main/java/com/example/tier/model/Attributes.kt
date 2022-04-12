package com.example.tier.model

data class Attributes(
    val batteryLevel: Number,
    val lat: Double,
    val lng: Double,
    val maxSpeed: Number,
    val vehicleType: String,
    val hasHelmetBox: Boolean
)
