package com.example.jettrivi.di

import com.example.jettrivi.network.QuestionApi
import com.example.jettrivi.repositories.Repository
import com.example.jettrivi.utils.Constants
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import  dagger.Module
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesQuestionRepository(api: QuestionApi) = Repository(api)

    @Singleton
    @Provides
    fun providesQuestionApi(): QuestionApi {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(QuestionApi::class.java)
    }


}