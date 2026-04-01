package com.example.euclydia.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.euclydia.model.Shape
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.InternalSerializationApi
import java.util.UUID
import kotlin.uuid.Uuid

@Dao
interface ShapeDao {
    @OptIn(InternalSerializationApi::class)
    @Query("SELECT * FROM Shapes")
    fun getAll() : Flow<List<DNA>>

    @OptIn(InternalSerializationApi::class)
    @Query("SELECT * FROM Shapes WHERE uuid=:uuid LIMIT 1")
    fun getShape(uuid: UUID): DNA?

    @OptIn(InternalSerializationApi::class)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(newShapes : List<DNA>)

    @OptIn(InternalSerializationApi::class)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(newShape : DNA)

    @OptIn(InternalSerializationApi::class)
    @Upsert
    suspend fun upsertAll(shapes : List<DNA>)

    @Query("DELETE FROM Shapes WHERE uuid IN (:uuids)")
    fun deleteIDs(uuids : List<UUID>)

    @Query("DELETE FROM Shapes")
    fun clear()

}