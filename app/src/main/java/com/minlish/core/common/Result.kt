package com.minlish.core.common

/**
 * Wrapper class for handling success and failure cases
 * This is useful for exposing return values to UI layer
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()

    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    fun exceptionOrNull(): Exception? = when (this) {
        is Error -> exception
        else -> null
    }

    inline fun <R> map(transform: (T) -> R): Result<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> Error(exception)
        is Loading -> Loading
    }

    inline fun onSuccess(block: (T) -> Unit): Result<T> {
        if (this is Success) block(data)
        return this
    }

    inline fun onError(block: (Exception) -> Unit): Result<T> {
        if (this is Error) block(exception)
        return this
    }
}

