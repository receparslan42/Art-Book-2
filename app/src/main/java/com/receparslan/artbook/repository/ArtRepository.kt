package com.receparslan.artbook.repository

import androidx.lifecycle.LiveData
import com.receparslan.artbook.apiService.ArtApiService
import com.receparslan.artbook.database.ArtDao
import com.receparslan.artbook.database.ArtDatabase
import com.receparslan.artbook.model.Art
import com.receparslan.artbook.model.ImageUrls
import com.receparslan.artbook.util.Resource
import javax.inject.Inject

class ArtRepository
@Inject
constructor(artDatabase: ArtDatabase, private val artApiService: ArtApiService) :
    ArtRepositoryInterface {

    private val artDao: ArtDao = artDatabase.artDao()

    override fun getAllArts(): LiveData<List<Art>> = artDao.getAllArts()

    override suspend fun insertArt(art: Art) = artDao.insertArt(art)

    override suspend fun deleteArt(art: Art) = artDao.deleteArt(art)

    override suspend fun getArtUrlList(searchQuery: String): Resource<List<ImageUrls>> {
        return try {
            val response = artApiService.getArtUrlList(searchQuery)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    Resource.success(apiResponse.imageUrls)
                } ?: Resource.error("No data found")
            } else {
                Resource.error("Error: ${response.message()}")
            }
        } catch (e: Exception) {
            return Resource.error(e.message ?: "Unknown error")
        }
    }
}