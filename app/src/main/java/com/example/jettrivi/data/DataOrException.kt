package com.example.jettrivi.data

class DataOrException<T, Boolean, Exception>(
    var data: T? = null,
    var isLoading: Boolean? = null,
    var exception: Exception? = null
)