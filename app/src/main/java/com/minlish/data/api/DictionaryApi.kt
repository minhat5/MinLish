package com.minlish.data.api

import com.minlish.data.api.model.TraCauResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApi {
    @GET("WBBcwnwQpV89/s/{word}/eng")
    suspend fun getWordDefinition(@Path("word") word: String): TraCauResponse
}