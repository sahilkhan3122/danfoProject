package com.example.showfadriverletest.di


import android.app.Application
import android.content.Context
import android.util.Log
import com.example.showfadriverletest.common.ApiConstant
import com.example.showfadriverletest.common.ApiConstant.API_KEY
import com.example.showfadriverletest.common.ApiConstant.API_VALUE
import com.example.showfadriverletest.common.Constants.EditVehicleDetail
import com.example.showfadriverletest.common.Constants.UploadDoc
import com.example.showfadriverletest.network.ApiService
import com.example.showfadriverletest.network.BaseDataSource
import com.example.showfadriverletest.pref.MyDataStore
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(dataStore: MyDataStore): Retrofit {

        var context: Context? = null
        val httpClient = HttpLoggingInterceptor()
        httpClient.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okhttp = OkHttpClient.Builder()
        okhttp.connectTimeout(5, TimeUnit.MINUTES) // connect timeout
            .writeTimeout(5, TimeUnit.MINUTES) // write timeout
            .readTimeout(5, TimeUnit.MINUTES)
        okhttp.addNetworkInterceptor(Interceptor { chain ->

            val token = runBlocking {
                dataStore.getFcmToken.first()
            }

            val apikey = runBlocking {
                dataStore.getApiKey.first()
            }
            Log.d("Token :- ", token)
            Log.d("Token :- ", apikey)

            chain.proceed(chain.request().newBuilder().also {
                /* if (apikey == "null" || apikey.isNullOrEmpty() || EditVehicleDetail || UploadDoc) {
                     it.addHeader("x-api-key", apikey)
                 }
                 it.addHeader(API_KEY, API_VALUE)*/
                if (apikey.isNotEmpty()) {
                    it.addHeader("x-api-key", apikey)
                }
                it.addHeader(API_KEY, API_VALUE)
            }.build())
        })


        /*
         if (apiKey.isNotEmpty()) {
                            requestBuilder.header(X_API_KEY, apiKey)
                        }
                        requestBuilder.header(
                            HEADER_KEY,
                            HEADER_VALUE
                        )

                        val request = requestBuilder.build()
                        return chain.proceed(request)
        * */

        return Retrofit.Builder()
            .baseUrl(ApiConstant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).client(okhttp.build()).build()
    }

    @Provides
    @Singleton
    fun providesApi(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideMyDataStore(context: Context): MyDataStore {
        return MyDataStore(context)
    }

    @Provides
    @Singleton
    fun providesBaseDataSource(): BaseDataSource {
        return BaseDataSource()
    }


    /*@Provides
    @Singleton
    fun provideUserPref(sharedPreferences: SharedPreferences, context: Context): UserPreference {
        return UserPreference(sharedPreferences, context)
    }*/
}
