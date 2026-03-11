package com.example.euclydia.viewmodel

import com.example.euclydia.model.DNA
import kotlinx.serialization.InternalSerializationApi

data class World @OptIn(InternalSerializationApi::class) constructor(
    val shapeList: List<DNA>,
    val worldWidth: Int,
    val worldHeight: Int,
    val tickCount: Int
)