package com.apps.fatal.data.di

import com.apps.fatal.data.api.PlaylistApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
internal class RetrofitModule {

    private companion object {

        const val BASE_URL = "https://limehd.online/"
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideConverterFactory(gson: Gson): Converter.Factory = GsonConverterFactory.create(gson)

    @Provides
    @Singleton
    fun provideOkHttpBuilder(): OkHttpClient.Builder = OkHttpClient.Builder()
        .callTimeout(2, TimeUnit.MINUTES)
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)

    @Provides
    @Singleton
    fun provideOkHttp(
        okHttpBuilder: OkHttpClient.Builder
    ): OkHttpClient = okHttpBuilder.apply {
        //добовить интерсептор
    }.build()

    @Provides
    @Singleton
    fun provideRetrofitBuilder(client: OkHttpClient, converterFactory: Converter.Factory): Retrofit.Builder = Retrofit.Builder()
        .client(client)
        .addConverterFactory(converterFactory)
        .baseUrl(BASE_URL)

    @Provides
    @Singleton
    fun providePlaylistApi(builder: Retrofit.Builder): PlaylistApi = builder
        .build()
        .create(PlaylistApi::class.java)
}