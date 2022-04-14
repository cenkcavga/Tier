package com.example.tier.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tier.model.VehicleResponse
import com.example.tier.network.NetworkResult
import com.example.tier.repository.VehicleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class VehicleMapViewModel
@Inject constructor(private val vehicleRepository: VehicleRepository): ViewModel() {

    // Need to keep mutable live data private to avoid giving view unnecessary access for repo etc.
    private val mutableLiveData = MutableLiveData<NetworkResult<VehicleResponse>>()

    // Live data will be public for view use
    val liveData : LiveData<NetworkResult<VehicleResponse>>
        get() = mutableLiveData

    init {
        getVehiclesOnMap()
    }

     fun getVehiclesOnMap(){
        viewModelScope.launch {
            vehicleRepository.getVehiclesOnLocation().let {
             mutableLiveData.value = it
            }
        }
    }


}