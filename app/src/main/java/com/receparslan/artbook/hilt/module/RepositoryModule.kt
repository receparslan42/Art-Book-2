package com.receparslan.artbook.hilt.module

import com.receparslan.artbook.apiService.ArtApiService
import com.receparslan.artbook.database.ArtDatabase
import com.receparslan.artbook.repository.ArtRepository
import com.receparslan.artbook.repository.ArtRepositoryInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideArtRepository(artDatabase: ArtDatabase, artApiService: ArtApiService): ArtRepositoryInterface =
        ArtRepository(artDatabase, artApiService)
}