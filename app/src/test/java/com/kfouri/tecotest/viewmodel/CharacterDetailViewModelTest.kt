package com.kfouri.tecotest.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kfouri.tecotest.api.ICharacterService
import com.kfouri.tecotest.api.model.CharacterDetailResponse
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
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class CharacterDetailViewModelTest {

    private lateinit var viewModel: CharacterDetailViewModel
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

        viewModel = CharacterDetailViewModel(characterApiModel)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
        testCoroutineDispatcher.cleanupTestCoroutines()
        testCoroutineScope.cleanupTestCoroutines()
    }

    @Test
    fun getCharacterDetailSuccessful() = runBlocking {
        //Given
        val response = Resource(
            Status.SUCCESS,
            CharacterDetailResponse(1,
                        "Rick",
                        "none",
                        "Human",
                        "Male",
                        Location("Earth"),
                        Location("Earth"),
                        "image.png",
                        listOf("Ep1", "Ep2")
                ),
            ""
        )

        Mockito.`when`(characterService.getCharacterDetail("1"))
            .thenReturn(response)

        //When
        viewModel.getCharacterDetail("1")

        //Then
        viewModel.characterDetailLiveData.observeOnce {
            Assert.assertEquals(Status.SUCCESS, it.status)
        }
    }

    @Test
    fun getCharacterDetailError() = runBlocking {
        //Given
        val response = Resource(
            Status.ERROR,
            null,
            ""
        )

        Mockito.`when`(characterService.getCharacterDetail("1"))
            .thenReturn(response)

        //When
        viewModel.getCharacterDetail("1")

        //Then
        viewModel.characterDetailLiveData.observeOnce {
            Assert.assertEquals(Status.ERROR, it.status)
        }
    }
}