package com.example.tier.repository

import com.example.tier.model.VehicleResponse
import com.example.tier.network.NetworkResult
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class TestVehicleRepository {

    private lateinit var  fakeRepository : FakeVehicleRepository

    @Before
    fun setUp() {
         fakeRepository = FakeVehicleRepository()
    }


    @Test
    fun apiResultReturnsNotNullObject(){
        runBlocking {
            fakeRepository.setShouldReturnNetworkError(true)
            val result = fakeRepository.getVehiclesOnLocation()
            assertThat(result).isNotNull()
        }
    }




    @Test
    fun successfulRequestDoesNotReturnsFailure(){
        runBlocking {
            fakeRepository.setShouldReturnNetworkError(true)
            val result = fakeRepository.getVehiclesOnLocation()
            assertThat(result).isNotInstanceOf(NetworkResult.Failure::class.java)
        }
    }



    @Test
    fun testReturnsSuccessfulResponse(){
        runBlocking {
            fakeRepository.setShouldReturnNetworkError(true)
            val result = fakeRepository.getVehiclesOnLocation()
            assertThat(result).isInstanceOf(NetworkResult.Success::class.java)
        }
    }



    @Test
    fun successfulResultReturnNonEmptyObject(){
        runBlocking {
            fakeRepository.setShouldReturnNetworkError(true)
            val result = fakeRepository.getVehiclesOnLocation()
                as NetworkResult.Success<VehicleResponse>
            assertThat(result.data).isNotNull()
        }
    }



    @Test
    fun successfulResultReturnsVehicleResponse(){
        runBlocking {
            fakeRepository.setShouldReturnNetworkError(true)
            val result = fakeRepository.getVehiclesOnLocation()
                as NetworkResult.Success<VehicleResponse>
            assertThat(result.data).isInstanceOf(VehicleResponse::class.java)
        }
    }



    @Test
    fun successfulResultReturnsNonEmptyVehicleResponseList(){
        runBlocking {
            fakeRepository.setShouldReturnNetworkError(true)
            val result = fakeRepository.getVehiclesOnLocation()
                as NetworkResult.Success<VehicleResponse>
            assertThat(result.data?.data).isNotEmpty()
        }
    }



    @Test
    fun testSuccessfulIfVehicleObjectsInListHasNonEmptyInfoParameter(){
        runBlocking {
            fakeRepository.setShouldReturnNetworkError(true)
            val result = fakeRepository.getVehiclesOnLocation()
                as NetworkResult.Success<VehicleResponse>
            assertThat(result.data?.data?.get(0)?.info).isNotNull()
        }
    }


    @Test
    fun failResultReturnsNonEmptyNetworkResult() {
        runBlocking {
            fakeRepository.setShouldReturnNetworkError(false)
            val result = fakeRepository.getVehiclesOnLocation()
                as NetworkResult.Failure<VehicleResponse>
            assertThat(result).isNotNull()
        }
    }


    @Test
    fun failResultReturnsException(){
            runBlocking {
                fakeRepository.setShouldReturnNetworkError(false)
                val result = fakeRepository.getVehiclesOnLocation()
                    as NetworkResult.Failure<VehicleResponse>
                assertThat(result.exception).isInstanceOf(Exception::class.java)
            }
    }


}