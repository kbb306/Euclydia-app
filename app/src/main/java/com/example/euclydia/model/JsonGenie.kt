package com.example.euclydia.model
import com.example.euclydia.database.DNA
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.UUID
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


object UUIDSerializer : KSerializer<UUID> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): UUID {
        return UUID.fromString(decoder.decodeString())
    }
}