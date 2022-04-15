package com.example.tier.viewmodel

import com.example.tier.model.VehicleResponse
import com.example.tier.network.NetworkResult
import com.example.tier.repository.FakeVehicleRepository
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class VehicleMapViewModelTest {

    lateinit var fakeVehicleRepository: FakeVehicleRepository
    lateinit var fakeVehicleMapViewModel: FakeVehicleMapViewModel


    @Before
    fun before(){
        fakeVehicleRepository = FakeVehicleRepository()
        fakeVehicleMapViewModel = FakeVehicleMapViewModel(fakeVehicleRepository)

    }

}