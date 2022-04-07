package com.kfouri.tecotest.model

import com.kfouri.tecotest.api.ICharacterService
import com.kfouri.tecotest.api.model.CharacterDetailResponse
import com.kfouri.tecotest.api.model.CharacterResponse
import com.kfouri.tecotest.state.Resource

class CharacterApiModel(private val characterService: ICharacterService): ICharacterApiModel {
    override suspend fun getCharacterList(page: String): Resource<CharacterResponse> {
        return characterService.getCharacterList(page)
    }

    override suspend fun getCharacterDetail(id: String): Resource<CharacterDetailResponse> {
        return characterService.getCharacterDetail(id)
    }


}