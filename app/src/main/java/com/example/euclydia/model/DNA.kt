package com.example.euclydia.model

import java.util.UUID
data class DNA (
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