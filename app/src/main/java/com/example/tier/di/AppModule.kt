package com.example.tier.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.tier.Constant
import com.example.tier.base.InternetException
import com.example.tier.network.ApiService
import com.example.tier.network.AppCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
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
    fun provideInterceptor(
        @ApplicationContext app: Context,
    ): Interceptor {
        return Interceptor { chain ->
            if(!isOnline(app))
                 throw InternetException()
            val url: HttpUrl = chain.request().url()
                .newBuilder()
                .addQueryParameter(Constant.API_KEY_TAG, Constant.API_KEY_VALUE)
                .build()
            val requestToCall= chain.request().newBuilder().url(url).build()
            chain.proceed(requestToCall)
        }
    }


    @Singleton
    @Provides
    @Named("ApiService")
    fun provideRetrofit(
        @Named("adapterFactory")
        appCallAdapterFactory: AppCallAdapterFactory,
        client: OkHttpClient
    ): ApiService =
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .client(client)
            .addCallAdapterFactory(appCallAdapterFactory)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)



    @Singleton
    @Provides
    @Named("adapterFactory")
    fun providesAppCallAdapterFactory(): AppCallAdapterFactory {
        return AppCallAdapterFactory()
    }


    /*
      Creates a client with customized url for api key query client
     */
    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        okHttpClientBuilder
             .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .addInterceptor (interceptor)
        return okHttpClientBuilder.build()
    }


    /*
        Checks if device is on ine to control before
        each api call

     */
    private fun isOnline (app: Context): Boolean {
        val conManager =app.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val network = conManager.activeNetwork ?: return false

            val activeNetwork = conManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            // Below M android level for deprecation
            @Suppress("DEPRECATION") val networkInfo =
                conManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }


}