package com.minlish.data.api

import com.minlish.domain.model.WordResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApi {
    @GET("entries/en/{word}")
    suspend fun getWordDefinition(@Path("word") word: String): List<WordResponse>
}