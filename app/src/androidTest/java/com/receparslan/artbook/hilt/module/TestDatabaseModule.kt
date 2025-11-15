package com.receparslan.artbook.hilt.module

import android.content.Context
import androidx.room.Room
import com.receparslan.artbook.database.ArtDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object TestDatabaseModule {

    @Provides
    fun provideInMemoryRoomDatabase(@ApplicationContext context: Context): ArtDatabase =
        Room.inMemoryDatabaseBuilder(
            context,
            ArtDatabase::class.java
        ).allowMainThreadQueries().build()
}