package com.kfouri.tecotest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kfouri.tecotest.model.ICharacterApiModel

class ViewModelFactory(private val model: ICharacterApiModel) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CharacterListViewModel::class.java) -> {
                CharacterListViewModel(model) as T
            }
            modelClass.isAssignableFrom(CharacterDetailViewModel::class.java) -> {
                CharacterDetailViewModel(model) as T
            }
            else -> throw IllegalArgumentException("Unknown class name")
        }
    }

}