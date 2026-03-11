package com.example.euclydia.model
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import java.util.UUID

enum class Age {
    CHILD,
    ADULT
}

enum class Gender {
    MALE,
    FEMALE,
    ANDROGYNOUS
}

enum class SpecialVoice {
    SC,
    EU
}
class Shape (
    val uuid : UUID,
    name: String,
    val age: Age,
    val gender : Gender,
    color: Int,
    val sides: Int,
    length: Double,
    x : Double,
    y : Double,
    heading : Double,
    speed : Double,
    var lines : MutableList<String>,
    var canon : SpecialVoice? = null

) : Turtle(name,x,y,heading,speed,color) {

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

    constructor( // For import
        genes: DNA
    ) : this(genes.uuid,genes.name,genes.age,
        genes.gender,genes.color,genes.sides,
        genes.length,genes.x,genes.y,
        genes.heading,genes.speed,genes.lines,
        genes.canon)

    constructor( // For legacy import
        dna : List<Any>,
        voice : Speech.VoiceRecord = Speech.reverseBS(dna[7] as String)
    ) : this(UUID.randomUUID(),
        dna[0] as String,
        age = voice.age,
        gender = voice.gender,
        dna[5] as Int, dna[1] as Int,
        dna[2] as Double,
        dna[3] as Double, dna[4] as Double,
        dna[6] as Double,5.00,
        dna[8] as MutableList<String>,
        canon = voice.canon
    )



    val radius : Double = length/2*kotlin.math.sin((180/sides).toDouble())

    override fun update(worldHeight: Double, worldWidth: Double) {
        super.update(worldHeight, worldWidth)
        turn(1f.toDouble())
    }

    override fun draw(
        canvas: Canvas,
        paint: Paint,
        cameraX: Double,
        cameraY: Double
    ) {
        paint.color = color
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4f

        val path = Path()

        for (i in 0 until sides) {
            val angle = Math.toRadians(heading + i * 360f /sides)
            val px: Double = cameraX + (kotlin.math.cos(angle) * radius)
            val py: Double = cameraY + (kotlin.math.sin(angle) * radius)

            if (i==0) path.moveTo(px.toFloat(),py.toFloat()) else path.lineTo(px.toFloat(),py.toFloat())

            if (isFollowed) {
                val box = RectF(
                    (x - cameraX - radius - 12f).toFloat(),
                    (y - cameraY - radius - 12f).toFloat(),
                    (x + cameraX + radius + 12f).toFloat(),
                    (y + cameraY + radius + 12f).toFloat()
                )
                canvas.drawRect(box,paint)
            }
        }
    }

    data class SpeechRequest (
        val line : String,
        val age: Age,
        val gender: Gender,
        val canon: SpecialVoice?
    )

    fun say(): SpeechRequest {
        val nextLine = lines.random()
        val pass = SpeechRequest(nextLine,age,gender,canon)
        return pass
    }

    fun export(): List<Any?> {
        val dna = listOf(uuid,name,age,gender,color,sides,radius,x,y,heading,speed,lines,canon)
        return dna
    }
}