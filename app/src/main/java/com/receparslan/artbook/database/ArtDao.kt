package com.receparslan.artbook.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.receparslan.artbook.model.Art

@Dao
interface ArtDao {
    @Query("SELECT * FROM arts")
    fun getAllArts(): LiveData<List<Art>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArt(art: Art)

    @Delete
    suspend fun deleteArt(art: Art)
}