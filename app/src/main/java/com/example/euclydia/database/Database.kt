package com.example.euclydia.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlinx.serialization.InternalSerializationApi

@OptIn(InternalSerializationApi::class)
@Database(entities = [DNA::class], version = 1)
class Database {

    abstract class Database : RoomDatabase() {
        abstract fun dao() : Dao
    }
}