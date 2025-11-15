package com.receparslan.artbook.repository

import androidx.lifecycle.LiveData
import com.receparslan.artbook.model.Art
import com.receparslan.artbook.model.ImageUrls
import com.receparslan.artbook.util.Resource

interface ArtRepositoryInterface {
    fun getAllArts(): LiveData<List<Art>>

    suspend fun insertArt(art: Art)

    suspend fun deleteArt(art: Art)

    suspend fun getArtUrlList(searchQuery: String): Resource<List<ImageUrls>>
}