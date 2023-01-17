package com.example.jettrivi.network

import com.example.jettrivi.model.Question
import retrofit2.http.GET

interface QuestionApi {

    @GET("world.json")
    suspend fun getAllQuestion():Question

}