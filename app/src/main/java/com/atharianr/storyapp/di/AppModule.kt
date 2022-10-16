package com.atharianr.storyapp.di

import com.atharianr.storyapp.data.source.remote.RemoteDataSource
import com.atharianr.storyapp.data.source.remote.network.ApiService
import com.atharianr.storyapp.ui.auth.AuthViewModel
import com.atharianr.storyapp.utils.Constant.API_BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val remoteDataSourceModule = module {
    factory { RemoteDataSource(get()) }
}
val viewModelModule = module {
    viewModel { AuthViewModel(get()) }
}