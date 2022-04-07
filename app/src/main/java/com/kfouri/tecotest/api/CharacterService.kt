package com.kfouri.tecotest.api

import androidx.lifecycle.LiveData
import com.kfouri.tecotest.api.model.CharacterDetailResponse
import com.kfouri.tecotest.api.model.CharacterResponse
import com.kfouri.tecotest.state.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class CharacterService : ICharacterService {

    private val mAPIService: APIService? by lazy {
        ApiClient.apiService
    }

    override suspend fun getCharacterList(page: String): Resource<CharacterResponse> {
        return withContext(Dispatchers.IO) {
            return@withContext mAPIService?.getCharacters(page)?.parse()!!
        }
    }

    override suspend fun getCharacterDetail(id: String): Resource<CharacterDetailResponse> {
        return withContext(Dispatchers.IO) {
            return@withContext mAPIService?.getCharacterDetail(id)?.parse()!!
        }
    }

    private fun <Data> Response<Data>.parse(): Resource<Data> {
        if (this.isSuccessful) {
            return when (this.code()) {
                200 -> {
                    this.body()?.let { data ->
                        Resource.success(data)
                    } ?: run {
                        Resource.error(this.body(), "Error")
                    }
                }
                else -> {
                    Resource.error(this.body(), "Error")
                }
            }
        } else {
            val message = errorBody()?.string() ?: "unknown error"
            return Resource.error(this.body(), message)
        }
    }
}
