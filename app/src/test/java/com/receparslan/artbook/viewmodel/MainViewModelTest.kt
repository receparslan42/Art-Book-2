package com.receparslan.artbook.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.receparslan.artbook.getOrAwaitValue
import com.receparslan.artbook.model.Art
import com.receparslan.artbook.repository.FakeArtRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel


    // Test setup method to initialize the ViewModel
    @Before
    fun setup() {
        viewModel = MainViewModel(FakeArtRepository()) // Initialize the ViewModel with a FakeArtRepository
    }

    @Test
    fun insertArtWithoutName_returnsError() {
        val art = Art(
            name = "",
            artist = "Artist Name",
            year = "2023",
            imageUrl = "https://example.com/image.jpg"
        )

        viewModel.makeArt(art)

        val status = viewModel.status.getOrAwaitValue()

        assertThat(status).isNotEmpty()
        assertThat(status["field"]).isEqualTo("name")
        assertThat(status["message"]).isEqualTo("Please enter a name for the art.")
    }

    @Test
    fun insertArtWithoutArtist_returnsError() {
        val art = Art(
            name = "Art Name",
            artist = "",
            year = "2025",
            imageUrl = "https://example.com/image.jpg"
        )

        viewModel.makeArt(art)

        val status = viewModel.status.getOrAwaitValue()

        assertThat(status).isNotEmpty()
        assertThat(status["field"]).isEqualTo("artist")
        assertThat(status["message"]).isEqualTo("Please enter an artist name for the art.")
    }

    @Test
    fun insertArtWithoutYear_returnsError() {
        val art = Art(
            name = "Art Name",
            artist = "Artist Name",
            year = "",
            imageUrl = "https://example.com/image.jpg"
        )

        viewModel.makeArt(art)

        val status = viewModel.status.getOrAwaitValue()

        assertThat(status).isNotEmpty()
        assertThat(status["field"]).isEqualTo("year")
        assertThat(status["message"]).isEqualTo("Please enter a year for the art.")
    }

    @Test
    fun insertArtWithoutImage_returnsError() {
        val art = Art(
            name = "Art Name",
            artist = "Artist Name",
            year = "2025",
            imageUrl = ""
        )

        viewModel.makeArt(art)

        val status = viewModel.status.getOrAwaitValue()

        assertThat(status).isNotEmpty()
        assertThat(status["field"]).isEqualTo("image")
        assertThat(status["message"]).isEqualTo("Please select an image for the art.")
    }

    @Test
    fun insertArtWithAllFields_returnsSuccess() {
        val art = Art(
            name = "Art Name",
            artist = "Artist Name",
            year = "2025",
            imageUrl = "https://example.com/image.jpg"
        )

        viewModel.makeArt(art)

        val status = viewModel.status.getOrAwaitValue()

        assertThat(status).isNotEmpty()
        assertThat(status["field"]).isEqualTo("success")
        assertThat(status["message"]).isEqualTo("Art created successfully!")
    }
}