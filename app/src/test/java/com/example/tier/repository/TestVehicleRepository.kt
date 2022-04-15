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
    fun apiResultReturnsNotNullObject() {
        runBlocking {
            val result = fakeRepository.getVehiclesOnLocation()
            assertThat(result).isNotNull()
        }
    }




    @Test
    fun successfulRequestDoesNotReturnFailure() {
        runBlocking {
            fakeRepository.setShouldReturnSuccess(true)
            val result = fakeRepository.getVehiclesOnLocation()
            assertThat(result).isNotInstanceOf(NetworkResult.Failure::class.java)
        }
    }



    @Test
    fun testReturnsSuccessfulResponse(){
        runBlocking {
            fakeRepository.setShouldReturnSuccess(true)
            val result = fakeRepository.getVehiclesOnLocation()
            assertThat(result).isInstanceOf(NetworkResult.Success::class.java)
        }
    }



    @Test
    fun successfulResultReturnNonEmptyObject(){
        runBlocking {
            fakeRepository.setShouldReturnSuccess(true)
            val result = fakeRepository.getVehiclesOnLocation()
                as NetworkResult.Success<VehicleResponse>
            assertThat(result.data).isNotNull()
        }
    }



    @Test
    fun successfulResultReturnsVehicleResponse(){
        runBlocking {
            fakeRepository.setShouldReturnSuccess(true)
            val result = fakeRepository.getVehiclesOnLocation()
                as NetworkResult.Success<VehicleResponse>
            assertThat(result.data).isInstanceOf(VehicleResponse::class.java)
        }
    }



    @Test
    fun successfulResultReturnsNonEmptyVehicleResponseList(){
        runBlocking {
            fakeRepository.setShouldReturnSuccess(true)
            val result = fakeRepository.getVehiclesOnLocation()
                as NetworkResult.Success<VehicleResponse>
            assertThat(result.data?.data).isNotEmpty()
        }
    }



    @Test
    fun successfulIfVehicleObjectsInListHasNonEmptyInfoParameter(){
        runBlocking {
            fakeRepository.setShouldReturnSuccess(true)
            val result = fakeRepository.getVehiclesOnLocation()
                as NetworkResult.Success<VehicleResponse>
            assertThat(result.data?.data?.get(0)?.info).isNotNull()
        }
    }


    @Test
    fun failStateReturnsNotNullNetworkResult() {
        runBlocking {
            fakeRepository.setShouldReturnSuccess(false)
            val result = fakeRepository.getVehiclesOnLocation()
                as NetworkResult.Failure<VehicleResponse>
            assertThat(result).isNotNull()
        }
    }


    @Test
    fun failResultReturnsException(){
            runBlocking {
                fakeRepository.setShouldReturnSuccess(false)
                val result = fakeRepository.getVehiclesOnLocation()
                    as NetworkResult.Failure<VehicleResponse>
                assertThat(result.exception).isInstanceOf(Exception::class.java)
            }
    }


}