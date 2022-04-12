package com.example.tier.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.example.tier.Constant
import com.example.tier.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideContext(app: Application): Context = app.applicationContext

    @Singleton
    @Provides
    fun provideResources(app: Application): Resources = app.resources


    @Singleton
    @Provides
    @Named("ApiService")
    fun provideRetrofit(
    ): ApiService =
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .client(createClientAuth())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)





    /*
      Creates a client with customized url for api key query
     */
    private fun createClientAuth(): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()

        okHttpClientBuilder.addInterceptor { chain ->
            val url: HttpUrl = chain.request().url()
                .newBuilder()
                .addQueryParameter(Constant.API_KEY_TAG, Constant.API_KEY_VALUE).
                build()
           val requestToCall= chain.request().newBuilder().url(url).build()
            chain.proceed(requestToCall)
        }

        return okHttpClientBuilder.build()
    }


}