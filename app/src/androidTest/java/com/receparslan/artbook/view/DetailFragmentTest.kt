package com.receparslan.artbook.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.receparslan.artbook.R
import com.receparslan.artbook.getOrAwaitValue
import com.receparslan.artbook.launchFragmentInHiltContainer
import com.receparslan.artbook.model.Art
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
class DetailFragmentTest {

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
    fun testNavigationFromDetailToSearchFragment() {

        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<DetailFragment>(factory = artFragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
        }

        Espresso.onView(ViewMatchers.withId(R.id.artImageView)).perform(click())

        Mockito.verify(navController).navigate(DetailFragmentDirections.actionDetailFragmentToSearchFragment())
    }

    @Test
    fun testOnBackPressed() {
        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<DetailFragment>(factory = artFragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
        }

        Espresso.pressBack()

        Mockito.verify(navController).popBackStack()
    }

    @Test
    fun testSave() {
        launchFragmentInHiltContainer<DetailFragment>(factory = artFragmentFactory) {
            viewModel = testViewModel
        }

        val art = Art(null, "Test Name", "Test Artist", "2023", "https://example.com/image.jpg")

        Espresso.onView(ViewMatchers.withId(R.id.nameEditText)).perform(ViewActions.typeText(art.name))
        Espresso.onView(ViewMatchers.withId(R.id.artistEditText)).perform(ViewActions.typeText(art.artist))
        Espresso.onView(ViewMatchers.withId(R.id.yearEditText)).perform(ViewActions.typeText(art.year))
        testViewModel.setSelectedImageUrls(ImageUrls("", "https://example.com/image.jpg"))
        Espresso.onView(ViewMatchers.withId(R.id.saveButton)).perform(click())

        val artList = testViewModel.artList.getOrAwaitValue()

        assertThat(artList).contains(art)
    }
}