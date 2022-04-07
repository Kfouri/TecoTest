package com.kfouri.tecotest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kfouri.tecotest.api.model.CharacterResponse
import com.kfouri.tecotest.model.ICharacterApiModel
import com.kfouri.tecotest.state.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CharacterListViewModel(
    private val model: ICharacterApiModel
): ViewModel(), CoroutineScope {

    override val coroutineContext = Job()
    private val characterListMutableLiveData = MutableLiveData<Resource<CharacterResponse>>()

    var currentPage = 1L
    var totalPages = 1L

    val characterListLiveData: LiveData<Resource<CharacterResponse>>
        get() = characterListMutableLiveData

    fun getCharacterList() {
        launch {
            characterListMutableLiveData.postValue(Resource.loading(data = null))
            try {
                characterListMutableLiveData.postValue(
                    model.getCharacterList(currentPage.toString())
                )
            } catch (e: Exception) {
                characterListMutableLiveData.postValue(
                    Resource.error(data = null, message = e.message ?: "Error getting Characters")
                )
            }
        }
    }
}