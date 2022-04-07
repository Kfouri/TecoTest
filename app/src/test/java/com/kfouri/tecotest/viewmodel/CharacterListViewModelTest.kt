package com.kfouri.tecotest.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.kfouri.tecotest.api.ICharacterService
import com.kfouri.tecotest.api.model.CharacterModel
import com.kfouri.tecotest.api.model.CharacterResponse
import com.kfouri.tecotest.api.model.Info
import com.kfouri.tecotest.api.model.Location
import com.kfouri.tecotest.extension.observeOnce
import com.kfouri.tecotest.model.CharacterApiModel
import com.kfouri.tecotest.model.ICharacterApiModel
import com.kfouri.tecotest.state.Resource
import com.kfouri.tecotest.state.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class CharacterListViewModelTest {

    private lateinit var viewModel: CharacterListViewModel
    private lateinit var characterService: ICharacterService
    private lateinit var characterApiModel: ICharacterApiModel

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    private val testCoroutineScope = TestCoroutineScope(testCoroutineDispatcher)

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun onBefore() {
        Dispatchers.setMain(testCoroutineDispatcher)

        characterService = Mockito.mock(ICharacterService::class.java)
        characterApiModel = CharacterApiModel(characterService)

        viewModel = CharacterListViewModel(characterApiModel)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
        testCoroutineDispatcher.cleanupTestCoroutines()
        testCoroutineScope.cleanupTestCoroutines()
    }

    @Test
    fun getCharacterListSuccessful() = runBlocking {
        //Given
        val response = Resource(
            Status.SUCCESS,
            CharacterResponse(
                Info(1),
                listOf(
                    CharacterModel("1",
                    "Rick",
                    "none",
                    "Human",
                    "Male",
                    Location("Earth"),
                    "image.png")
                )),
            ""
        )

        Mockito.`when`(characterService.getCharacterList("1"))
            .thenReturn(response)

        //When
        viewModel.getCharacterList()

        //Then
        viewModel.characterListLiveData.observeOnce {
            Assert.assertEquals(Status.SUCCESS, it.status)
        }
    }

    @Test
    fun getCharacterListError() = runBlocking {
        //Given
        val response = Resource(
            Status.ERROR,
            null,
            ""
        )

        Mockito.`when`(characterService.getCharacterList("1"))
            .thenReturn(response)

        //When
        viewModel.getCharacterList()

        //Then
        viewModel.characterListLiveData.observeOnce {
            Assert.assertEquals(Status.ERROR, it.status)
        }
    }
}