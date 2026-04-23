package com.example.euclydia.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

object ShapeStore {
    private val _shapes = MutableStateFlow<List<Shape>>(emptyList())
    val shapes: StateFlow<List<Shape>> = _shapes.asStateFlow()

    fun setShapes(shapes: List<Shape>) {
        _shapes.value = shapes
    }

    fun addShape(shape: Shape) {
        _shapes.update { it + shape }
    }

    fun addShapes(shapes: List<Shape>) {
        _shapes.update { it + shapes }
    }


    fun removeShapes(shapes : List<UUID>) {
        _shapes.update { list -> list.filterNot { it.uuid in shapes } }
    }


    fun mutate(transform: (MutableList<Shape>) -> Unit) {
        val current = _shapes.value.toMutableList()
        transform(current)
        _shapes.value = current.toList()
    }

}