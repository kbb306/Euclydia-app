package com.example.euclydia.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlinx.serialization.InternalSerializationApi

@OptIn(InternalSerializationApi::class)
@Database(entities = [DNA::class], version = 1)
@TypeConverters(Polymerase::class)
abstract class EuclydiaDatabase : RoomDatabase() {
    abstract fun dao() : ShapeDao
    }
