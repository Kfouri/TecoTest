package com.kfouri.tecotest.api

import com.kfouri.tecotest.api.model.CharacterDetailResponse
import com.kfouri.tecotest.api.model.CharacterResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {

    @GET("character")
    suspend fun getCharacters(@Query("page") page: String): Response<CharacterResponse>

    @GET("character/{id}")
    suspend fun getCharacterDetail(@Path("id") id: String): Response<CharacterDetailResponse>
}