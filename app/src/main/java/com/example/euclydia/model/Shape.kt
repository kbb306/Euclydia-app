package com.example.euclydia.model
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF

class Shape (
    name: String,
    x : Double,
    y : Double,
    heading : Double,
    speed : Double,
    color: Int,
    var sides: Int,
    var radius : Double,
    voice : Int,
    age: Int
) : Turtle(name,x,y,heading,speed,color) {

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


}