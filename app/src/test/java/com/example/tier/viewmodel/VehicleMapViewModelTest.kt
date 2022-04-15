package com.example.tier.viewmodel


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.tier.CoroutineRuleTest
import com.example.tier.getOrAwaitValueTest
import com.example.tier.model.VehicleResponse
import com.example.tier.network.NetworkResult
import com.example.tier.repository.FakeVehicleRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class VehicleMapViewModelTest {

    @get: Rule
    var instantTaskExecutorRule= InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = CoroutineRuleTest()

    lateinit var fakeVehicleRepository: FakeVehicleRepository
    lateinit var fakeVehicleMapViewModel: FakeVehicleMapViewModel


    @Before
    fun before() {
        fakeVehicleRepository = FakeVehicleRepository()
        fakeVehicleMapViewModel = FakeVehicleMapViewModel(fakeVehicleRepository)

    }


    @Test
    fun getVehiclesReturnsNotNull() {
        fakeVehicleMapViewModel.getVehiclesOnMap()
        val result =  fakeVehicleMapViewModel.liveData.getOrAwaitValueTest()
        assertThat(result).isNotNull()
    }



    @Test
    fun getVehiclesReturnsNetworkResultType(){
        fakeVehicleMapViewModel.getVehiclesOnMap()
        val result =  fakeVehicleMapViewModel.liveData.getOrAwaitValueTest()
        assertThat(result).isInstanceOf(NetworkResult::class.java)
    }


    @Test
    fun getVehiclesReturnsFailureWhenRepoReturnsFailure(){
        fakeVehicleMapViewModel.getVehiclesOnMap()
        val result =  fakeVehicleMapViewModel.liveData.getOrAwaitValueTest()
        assertThat(result).isInstanceOf(NetworkResult.Failure::class.java)
    }


    @Test
    fun getVehiclesFailStateReturnsFailureWithException(){
        fakeVehicleRepository.setShouldReturnSuccess(false)
        fakeVehicleMapViewModel.getVehiclesOnMap()
        val result =  fakeVehicleMapViewModel.liveData.getOrAwaitValueTest()
            as NetworkResult.Failure<VehicleResponse>
        assertThat(result.exception).isNotNull()
        assertThat(result.exception).isInstanceOf(Exception::class.java)
    }


    @Test
    fun getVehiclesReturnsSuccess(){
        fakeVehicleRepository.setShouldReturnSuccess(true)
        fakeVehicleMapViewModel.getVehiclesOnMap()
        val result =  fakeVehicleMapViewModel.liveData.getOrAwaitValueTest()
        assertThat(result).isInstanceOf(NetworkResult.Success::class.java)
    }


    @Test
    fun getVehiclesReturnsVehicleResponse(){
        fakeVehicleRepository.setShouldReturnSuccess(true)
        fakeVehicleMapViewModel.getVehiclesOnMap()
        val result =  fakeVehicleMapViewModel.liveData.getOrAwaitValueTest()
            as NetworkResult.Success<VehicleResponse>
        assertThat(result.data).isInstanceOf(VehicleResponse::class.java)
    }

    @Test
    fun getVehiclesReturnsNotNullVehicleResponse(){
        fakeVehicleRepository.setShouldReturnSuccess(true)
        fakeVehicleMapViewModel.getVehiclesOnMap()
        val result =  fakeVehicleMapViewModel.liveData.getOrAwaitValueTest()
            as NetworkResult.Success<VehicleResponse>
        assertThat(result.data).isNotNull()
    }

    @Test
    fun getVehiclesReturnsNotNullVehicleResponseHasNonEmptyVehicleList(){
        fakeVehicleRepository.setShouldReturnSuccess(true)
        fakeVehicleMapViewModel.getVehiclesOnMap()
        val result =  fakeVehicleMapViewModel.liveData.getOrAwaitValueTest()
            as NetworkResult.Success<VehicleResponse>
        assertThat(result.data?.data).isNotEmpty()
    }


}