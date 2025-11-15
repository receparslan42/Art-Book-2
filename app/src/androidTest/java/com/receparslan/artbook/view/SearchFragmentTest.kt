package com.receparslan.artbook.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.receparslan.artbook.R
import com.receparslan.artbook.adapter.SearchRecyclerAdapter
import com.receparslan.artbook.getOrAwaitValue
import com.receparslan.artbook.launchFragmentInHiltContainer
import com.receparslan.artbook.model.ImageUrls
import com.receparslan.artbook.repository.FakeArtRepository
import com.receparslan.artbook.viewmodel.MainViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
class SearchFragmentTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var artFragmentFactory: ArtFragmentFactory

    private lateinit var testViewModel: MainViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        testViewModel = MainViewModel(FakeArtRepository())
    }

    @Test
    fun testSelectImage() {
        val navController = Mockito.mock(NavController::class.java)

        val imageUrls = ImageUrls("https://example.com/image.jpg", "https://example.com/preview.jpg")

        launchFragmentInHiltContainer<SearchFragment>(factory = artFragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
            viewModel = testViewModel

            searchRecyclerAdapter.urls = listOf(imageUrls)
        }

        Espresso.onView(withId(R.id.searchRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<SearchRecyclerAdapter.SearchViewHolder>(0, click())
        )

        val selectedImage = testViewModel.selectedImageUrls.getOrAwaitValue()

        Mockito.verify(navController).popBackStack()
        assertThat(selectedImage).isEqualTo(imageUrls)
    }
}