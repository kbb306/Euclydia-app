package com.example.euclydia.database

import androidx.room.Insert
import androidx.room.Query
import com.example.euclydia.model.Shape
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.InternalSerializationApi
import java.util.UUID
import kotlin.uuid.Uuid

interface Dao {
    @OptIn(InternalSerializationApi::class)
    @Query("SELECT * FROM Shapes")
    fun getAll() : Flow<List<Shape>> {
        TODO("Get data from query and construct shapeList")
    }

    @Query("SELECT * FROM Shapes WHERE uuid=(:uuid)")
    fun getShape(uuid: UUID) : Shape {
        TODO("Get data from query and build shape")
    }

    @Insert
    suspend fun insert(new : List<Shape>)

    suspend fun sync(input : List<Shape>) : List<Shape>{
        TODO("Sync local Flowstate and database")
    }
}