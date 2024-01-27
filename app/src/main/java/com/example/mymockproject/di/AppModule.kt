package com.example.mymockproject.di

import android.app.Application
import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.mymockproject.BuildConfig
import com.example.mymockproject.database.db.AppDatabase
import com.example.mymockproject.network.ApiDataSource
import com.example.mymockproject.network.ApiFilmService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    private fun getHttpClient(context: Context): OkHttpClient {
        val client = OkHttpClient.Builder().also { client ->
            if(BuildConfig.DEBUG){
                val loggingContent = HttpLoggingInterceptor()
                loggingContent.setLevel(HttpLoggingInterceptor.Level.BODY)
                val collector = ChuckerCollector(context)
                val chuckerInterceptor = ChuckerInterceptor.Builder(context)
                    .alwaysReadResponseBody(true)
                    .collector(collector)
                    .build()
                client.interceptors().add(chuckerInterceptor)
                client.interceptors().add(loggingContent)
            }
        }.build()
        return client
    }

    @Singleton
    @Provides
    fun provideRetrofit(context: Application): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getHttpClient(context))
            .build()
    }

    @Singleton
    @Provides
    fun provideApiFilmService(retrofit: Retrofit) = retrofit.create(ApiFilmService::class.java)

    @Singleton
    @Provides
    fun provideApiDataSource(apiFilmService: ApiFilmService) = ApiDataSource(apiFilmService)

    @Singleton
    @Provides
    fun provideAppDatabase(context: Application) = AppDatabase.getInstance(context)

    @Singleton
    @Provides
    fun provideFavouriteDao(db: AppDatabase) = db.movieDao()
}