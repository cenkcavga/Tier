package com.example.tier.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tier.model.VehicleResponse
import com.example.tier.network.NetworkResult
import com.example.tier.repository.FakeVehicleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FakeVehicleMapViewModel
@Inject constructor(private val fakeVehicleRepository: FakeVehicleRepository ): ViewModel() {

    // Need to keep mutable live data private to avoid giving view unnecessary access for repo etc.
    private val mutableLiveData = MutableLiveData<NetworkResult<VehicleResponse>>()

    // Live data will be public for view use
    val liveData : LiveData<NetworkResult<VehicleResponse>>
        get() = mutableLiveData

    init {
        getVehiclesOnMap()
    }

    fun getVehiclesOnMap() = viewModelScope.launch {
            fakeVehicleRepository.getVehiclesOnLocation().let { response ->
                mutableLiveData.value = response

            }
    }


}