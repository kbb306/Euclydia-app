package com.example.euclydia.database

import android.content.Context
import androidx.room.Room
import com.example.euclydia.model.Shape
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.InternalSerializationApi
import java.util.UUID

class Repository  private constructor(context: Context) {
    private val db : EuclydiaDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            EuclydiaDatabase::class.java,
            "euclydia")
        .createFromAsset("euclydia.db")
        .build()

    companion object {
        private var INSTANCE : Repository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = Repository(context)
            }
        }

        fun get() : Repository {
            return INSTANCE ?: throw IllegalStateException()
        }
    }
    @OptIn(InternalSerializationApi::class)
    val shapeList : Flow<List<Shape>> = db.dao().getAll().map { dnaList ->
        dnaList.map { dna -> Shape(dna) }
    }

    @OptIn(InternalSerializationApi::class)
    suspend fun getShape(uuid: UUID): Shape? {
        return db.dao().getShape(uuid)?.let { dna ->
            Shape(dna)
        }
    }

    @OptIn(InternalSerializationApi::class)
    suspend fun insertShapes(shapes : List<Shape>) = db.dao().insertAll(shapes.map {it.export()})

    @OptIn(InternalSerializationApi::class)
    suspend fun insertShape(shape : Shape) {
        db.dao().insertOne(shape.export())
    }

    @OptIn(InternalSerializationApi::class)
    suspend fun sync(input: List<Shape>) {
        db.dao().upsertAll(input.map { it.export() })
    }

    suspend fun deleteIDs(uuids: List<UUID>) {
        db.dao().deleteIDs(uuids)
    }

    suspend fun clear() {
        db.dao().clear()
    }
}
