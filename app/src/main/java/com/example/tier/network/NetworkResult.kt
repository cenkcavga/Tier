package com.example.tier.network

// Custom response in Success Fail format
sealed class NetworkResult<out T> {
    data class Success<T>(var data: T?) : NetworkResult<T>()
    data class Failure<T>(val statusCode: Int = -1,
                          val exception: Throwable? = null) : NetworkResult<T>()
}