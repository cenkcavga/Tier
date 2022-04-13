package com.example.tier.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tier.model.Vehicle
import com.example.tier.repository.VehicleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject




@HiltViewModel
class VehicleMapViewModel
@Inject constructor(private val vehicleRepository: VehicleRepository): ViewModel() {

    // Need to keep mutable live data private to avoid giving view unnecessary access for repo etc.
    private val mutableLiveData = MutableLiveData<List<Vehicle>>()

    // Live data will be public for view use
    val liveData : LiveData<List<Vehicle>>
        get() = mutableLiveData

    init {
        getVehiclesOnMap()
    }

    private fun getVehiclesOnMap(){
        viewModelScope.launch {
            vehicleRepository.getVehiclesOnLocation().let { response ->
                if (response.isSuccessful)
                    mutableLiveData.value = response.body()?.data
                else
                    Log.v("Vehicles On List", "$response.code response.message()")

            }
        }
    }




}