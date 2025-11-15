package com.receparslan.artbook.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.receparslan.artbook.model.Art
import com.receparslan.artbook.model.ImageUrls
import com.receparslan.artbook.util.Resource

class FakeArtRepository : ArtRepositoryInterface {

    private val arts = mutableListOf<Art>()
    private val artLiveData = MutableLiveData<List<Art>>(arts)

    override fun getAllArts(): LiveData<List<Art>> = artLiveData

    override suspend fun insertArt(art: Art) {
        arts.add(art)
        refreshData()
    }

    override suspend fun deleteArt(art: Art) {
        arts.remove(art)
        refreshData()
    }

    override suspend fun getArtUrlList(searchQuery: String): Resource<List<ImageUrls>> {
        return Resource.success(listOf(ImageUrls("https://example.com/image1.jpg", "https://example.com/image2.jpg")))
    }

    private fun refreshData() {
        artLiveData.postValue(arts)
    }
}