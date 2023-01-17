package com.example.jettrivi.repositories

import android.util.Log
import com.example.jettrivi.data.DataOrException
import com.example.jettrivi.model.QuestionItem
import com.example.jettrivi.network.QuestionApi
import javax.inject.Inject

class Repository @Inject constructor(private val api: QuestionApi) {

    private val dataOrException = DataOrException<ArrayList<QuestionItem>, Boolean, Exception>()

    suspend fun getAllQuestion(): DataOrException<ArrayList<QuestionItem>, Boolean, Exception> {
        try {

            dataOrException.isLoading = true;

            dataOrException.data = api.getAllQuestion();

            if (dataOrException.data.toString().isNotEmpty()) dataOrException.isLoading = false



        } catch (e: java.lang.Exception) {
            Log.d("Excp", e.toString())
        }

        return dataOrException;
    }

}