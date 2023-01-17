package com.example.jettrivi.screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jettrivi.data.DataOrException
import com.example.jettrivi.model.QuestionItem
import com.example.jettrivi.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val data: MutableState<DataOrException<ArrayList<QuestionItem>, Boolean, java.lang.Exception>> =
        mutableStateOf(
            DataOrException(
                null,
                true,
                Exception("")
            )
        )

    init {
        getAllQuestion()
    }

    private fun getAllQuestion() {

        viewModelScope.launch {
            data.value.isLoading = true

            data.value = repository.getAllQuestion()

            if (data.value.data.toString().isNotEmpty()) {
                data.value.isLoading = false
            }

        }
    }

}