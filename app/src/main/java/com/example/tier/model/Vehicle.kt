package com.example.tier.model


data class Vehicle(
    val id: String,
    val type: String,
    val attributes: Attributes,
    var info: String?
)
