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
    val uuid : String = UUID.randomUUID().toString(),
    name: String,
    val age: Age,
    val gender : Gender,
    color: Int,
    val sides: Int,
    val radius : Double,
    x : Double,
    y : Double,
    heading : Double,
    speed : Double,
    var lines : MutableList<String>,
    var canon : SpecialVoice? = null

) : Turtle(name,x,y,heading,speed,color) {

    constructor(dna : List<Any>) : this(dna[0] as String,
        dna[1] as String, dna[2] as Age, dna[3] as Gender,
        dna[4] as Int, dna[5] as Int, dna[6] as Double, dna[7] as Double,
        dna[8] as Double, dna[9] as Double, dna[10] as Double, dna[11] as MutableList<String>
    )




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

    fun say(): Triple<String, Gender, Age> {
        val nextLine = lines.random()
        val pass = Triple(nextLine,gender as Gender,age)
        return pass
    }

    fun export(): List<Any> {
        val dna = listOf(uuid,name,age,gender,color,sides,radius,x,y,heading,speed,lines)
        return dna
    }
}