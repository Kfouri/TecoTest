package com.kfouri.tecotest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kfouri.tecotest.api.model.CharacterDetailResponse
import com.kfouri.tecotest.model.ICharacterApiModel
import com.kfouri.tecotest.state.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CharacterDetailViewModel(
    private val model: ICharacterApiModel
): ViewModel(), CoroutineScope {

    override val coroutineContext = Job()
    private val characterDetailMutableLiveData = MutableLiveData<Resource<CharacterDetailResponse>>()

    val characterDetailLiveData: LiveData<Resource<CharacterDetailResponse>>
        get() = characterDetailMutableLiveData

    fun getCharacterDetail(id: String) {
        launch {
            characterDetailMutableLiveData.postValue(Resource.loading(data = null))
            try {
                characterDetailMutableLiveData.postValue(
                    model.getCharacterDetail(id)
                )
            } catch (e: Exception) {
                characterDetailMutableLiveData.postValue(
                    Resource.error(data = null, message = e.message ?: "Error getting Character Detail")
                )
            }
        }
    }
}