package com.kfouri.tecotest.model

import com.kfouri.tecotest.api.model.CharacterDetailResponse
import com.kfouri.tecotest.api.model.CharacterResponse
import com.kfouri.tecotest.state.Resource

interface ICharacterApiModel {
    suspend fun getCharacterList(page: String): Resource<CharacterResponse>
    suspend fun getCharacterDetail(id: String): Resource<CharacterDetailResponse>
}