package com.example.euclydia.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.euclydia.model.Shape

class Plane @JvmOverloads constructor(
    context: Context,
    attrs : AttributeSet? = null,
) : View(context,attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var shapes : List<Shape> = emptyList()

    fun submit(newShapes: List<Shape>) {
        shapes = newShapes
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)

        for (shape in shapes) {
            shape.draw(canvas,paint,0.0,0.0)
        }
    }
}