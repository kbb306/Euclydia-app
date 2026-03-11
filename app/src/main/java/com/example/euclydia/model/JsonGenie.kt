package com.example.euclydia.model
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
object ShapeJson {
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    @OptIn(InternalSerializationApi::class)
    fun encodeShape(shape: Shape): String {
        return json.encodeToString(shape.export())
    }

    @OptIn(InternalSerializationApi::class)
    fun decodeShape(text: String): Shape {
        return Shape(json.decodeFromString<DNA>(text))
    }

    @OptIn(InternalSerializationApi::class)
    fun encodeShapes(shapes: List<Shape>): String {
        return json.encodeToString(shapes.map { it.export() })
    }

    @OptIn(InternalSerializationApi::class)
    fun decodeShapes(text: String): List<Shape> {
        return json.decodeFromString<List<DNA>>(text).map { Shape(it) }
    }
}
