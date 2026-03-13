package com.example.euclydia.model
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import java.util.UUID
import kotlin.math.pow

@Serializable
enum class Age {
    CHILD,
    ADULT
}

@Serializable
enum class Gender {
    MALE,
    FEMALE,
    ANDROGYNOUS
}

@Serializable
enum class SpecialVoice {
    SC,
    EU
}

    class Shape(
        var uuid: UUID,
        name: String,
        val age: Age,
        val gender: Gender,
        color: Int,
        val sides: Int,
        val length: Double,
        x: Double,
        y: Double,
        heading: Double,
        speed: Double,
        var lines: MutableList<String>,
        var canon: SpecialVoice? = null

    ) : Turtle(name, x, y, heading, speed, color) {

        constructor( // The "Primary" constructor
            name: String, age: Age, gender: Gender,
            color: Int, sides: Int, length: Double,
            x: Double, y: Double, heading: Double,
            speed: Double, lines: MutableList<String>, canon: SpecialVoice? = null
        ) : this(
            UUID.randomUUID(), name, age,
            gender, color, sides,
            length, x, y,
            heading, speed, lines,
            canon
        )

        @OptIn(InternalSerializationApi::class)
        constructor( // For import
            genes: DNA
        ) : this(
            genes.uuid, genes.name, genes.age,
            genes.gender, genes.color, genes.sides,
            genes.length, genes.x, genes.y,
            genes.heading, genes.speed, genes.lines.toMutableList(),
            genes.canon
        )

        constructor( // For legacy import
            dna: List<Any>,
            voice: Speech.VoiceRecord = Speech.reverseBS(dna[7] as String)
        ) : this(
            UUID.randomUUID(),
            dna[0] as String,
            age = voice.age,
            gender = voice.gender,
            dna[5] as Int, dna[1] as Int,
            dna[2] as Double,
            dna[3] as Double, dna[4] as Double,
            dna[6] as Double, 5.00,
            dna[8] as MutableList<String>,
            canon = voice.canon
        )


        val radius: Double = length / 2 * kotlin.math.sin((Math.PI / sides))

        override fun update(worldHeight: Double, worldWidth: Double) {
            super.update(worldHeight, worldWidth)
            val randomTurn = (1..10).random()
            turnTo(heading + randomTurn)
        }

        fun avoid(other: Shape): Double { // Calculate a safe heading away from the other shape
            val dx = x - other.x
            val dy = y - other.y
            return Math.toDegrees(kotlin.math.atan2(dy, dx))
        }

        fun distance(other: Shape): Double {
            return kotlin.math.sqrt((other.x - this.x).pow(2) + (other.y - this.y).pow(2))
        }


        override fun draw(
            canvas: Canvas,
            paint: Paint,
            cameraX: Double,
            cameraY: Double
        ) {
            paint.color = color
            paint.style = Paint.Style.FILL
            paint.strokeWidth = 4f

            val path = Path()

            for (i in 0 until sides) {
                val angle = Math.toRadians(heading + i * 360.0 / sides)
                val px = x - cameraX + (kotlin.math.cos(angle) * radius)
                val py = y - cameraY + (kotlin.math.sin(angle) * radius)
                if (i == 0) path.moveTo(px.toFloat(), py.toFloat())
                else path.lineTo(
                    px.toFloat(),
                    py.toFloat()
                )
                path.close()
                canvas.drawPath(path, paint)

                if (isFollowed) {
                    val box = RectF(
                        (x - cameraX - radius - 12.0).toFloat(),
                        (y - cameraY - radius - 12.0).toFloat(),
                        (x + cameraX + radius + 12.0).toFloat(),
                        (y + cameraY + radius + 12.0).toFloat()
                    )
                    canvas.drawRect(box, paint)
                }
            }
        }

        data class SpeechRequest(
            val speakerName: String,
            val age: Age,
            val gender: Gender,
            val canon: SpecialVoice?
        )

        fun say(): SpeechRequest? {
            if ((0..100).random() < 1) {
                val pass = SpeechRequest(
                    speakerName = name,
                    age = age,
                    gender = gender,
                    canon = canon
                )
                return pass
            }
            return null
        }

        @OptIn(InternalSerializationApi::class)
        fun export(): DNA {
            return DNA(
                uuid, name, age,
                gender, color, sides,
                length, x, y,
                heading, speed, lines,
                canon
            )
        }
    }
