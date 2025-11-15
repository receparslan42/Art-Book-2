package com.receparslan.artbook.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.receparslan.artbook.getOrAwaitValue
import com.receparslan.artbook.model.Art
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@SmallTest
@HiltAndroidTest
class ArtDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var database: ArtDatabase

    private lateinit var dao: ArtDao

    @Before
    fun setup() {
        hiltRule.inject()

        dao = database.artDao()
    }

    @After
    fun tearDown() {
        database.close() // Close the database after each test
    }

    @Test
    fun insertArt() = runTest {
        // Given an Art object
        val art = Art(
            id = 1,
            name = "Tet Art",
            artist = "Test Artist",
            year = "2025",
            imageUrl = "http://example.com/image.jpg"
        )

        // When inserting the art into the database
        dao.insertArt(art)

        // Then retrieving it by ID should return the same object
        val retrievedArt = dao.getAllArts().getOrAwaitValue().firstOrNull()
        assertThat(art).isEqualTo(retrievedArt)
    }

    @Test
    fun deleteArt() = runTest {
        // Given an Art object
        val art = Art(
            id = 1,
            name = "Tet Art",
            artist = "Test Artist",
            year = "2025",
            imageUrl = "http://example.com/image.jpg"
        )

        // Insert the art into the database
        dao.insertArt(art)

        // When deleting the art
        dao.deleteArt(art)

        // Then retrieving it should return an empty list
        val arts = dao.getAllArts().getOrAwaitValue()
        assertThat(arts).isEmpty()
    }
}