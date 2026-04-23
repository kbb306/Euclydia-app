package com.example.euclydia.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ShapeDao {
    @Query("SELECT * FROM Shapes")
    fun getAll() : Flow<List<DNA>>

    @Query("SELECT * FROM Shapes WHERE uuid=:uuid LIMIT 1")
    fun getShape(uuid: UUID): DNA?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(newShapes : List<DNA>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(newShape : DNA)

    @Upsert
    suspend fun upsertAll(shapes : List<DNA>)

    @Query("DELETE FROM Shapes WHERE uuid IN (:uuids)")
    suspend fun deleteIDs(uuids : List<UUID>)

    @Query("DELETE FROM Shapes")
    suspend fun clear()

}