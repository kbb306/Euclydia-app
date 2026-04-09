package com.example.euclydia.database

import android.R
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.euclydia.model.Age
import com.example.euclydia.model.Gender
import com.example.euclydia.model.SpecialVoice
import kotlinx.serialization.Contextual
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import java.util.UUID

@InternalSerializationApi
@Serializable
@Entity(tableName = "Shapes")
data class DNA (
    @Contextual
    @PrimaryKey val uuid: UUID,
    val name: String,
    val age: Age,
    val gender: Gender,
    val color: Int,
    val sides: Int,
    val length: Double,
    val x: Double,
    val y: Double,
    val heading: Double,
    val speed: Double,
    val canon: SpecialVoice?
)
class Polymerase {
    @TypeConverter
    fun fromUUID(value : UUID?) : String? = value?.toString()
    @TypeConverter
    fun toUUID(value: String?) : UUID? = value?.let { UUID.fromString(it)}
}

