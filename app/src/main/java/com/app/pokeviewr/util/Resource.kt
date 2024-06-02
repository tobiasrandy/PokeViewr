package com.app.pokeviewr.util

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val loadingType: LoadingType? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(type: LoadingType = LoadingType.INITIAL) : Resource<T>(loadingType = type)
}

enum class LoadingType {
    INITIAL,
    PAGINATION,
    REFRESH
}