package com.example.euclydia.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import java.util.UUID
@InternalSerializationApi @Serializable
data class DNA (
    @Contextual
    val uuid: UUID,
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
    val lines: MutableList<String>,
    val canon: SpecialVoice?
)