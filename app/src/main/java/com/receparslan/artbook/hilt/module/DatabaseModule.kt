package com.receparslan.artbook.hilt.module

import android.content.Context
import com.receparslan.artbook.database.ArtDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideArtDao(@ApplicationContext context: Context): ArtDatabase = ArtDatabase(context)
}